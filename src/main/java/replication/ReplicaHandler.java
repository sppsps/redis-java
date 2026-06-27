package replication;

import context.RedisContext;
import dto.Replicas;
import dto.ReplicationInformation;
import dto.StringReader;
import dto.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.HashMap;

public class ReplicaHandler {
    private final Logger LOG = LogManager.getLogger(ReplicaHandler.class);
    private String[] args;
    ReplicationInformation replicationInformation;
    Socket masterSocket;
    public void handlePings() throws Exception {
        boolean isReplica = args[2].equals("--replicaof");
        replicationInformation.setReplica(isReplica);
        String[] master = args[3].split(" ");;
        replicationInformation.setHost(master[0]);
        replicationInformation.setMasterPort(Integer.parseInt(master[1]));
        try{
            masterSocket = new Socket(master[0], Integer.parseInt(master[1]));
        } catch(Exception e) {
            throw new Exception(e.getMessage());
        }
        DataOutputStream out = new DataOutputStream(masterSocket.getOutputStream());
        DataInputStream in = new DataInputStream(masterSocket.getInputStream());
        makePing(out, in);
        makeReplConf(out, in);
        makePsync(out, in);
        replicationInformation.setOut(out);
    }

    private void makePsync(DataOutputStream out, DataInputStream in) throws IOException {
        out.write("*3\r\n$5\r\nPSYNC\r\n$1\r\n?\r\n$2\r\n-1\r\n".getBytes());
        out.flush();
        in.readLine();
    }

    public void makePing(DataOutputStream out, DataInputStream in) throws IOException {
        out.write("*1\r\n$4\r\nPING\r\n".getBytes());
        out.flush();
        in.readLine();
    }

    public void makeReplConf(DataOutputStream out, DataInputStream in) throws IOException {
        int port = replicationInformation.getPort();
        out.write(("*3\r\n$8\r\nREPLCONF\r\n$14\r\nlistening-port\r\n$+"+"4"+"\r\n"+port+"\r\n").getBytes());
        out.flush();
        String resp1 = in.readLine();
        out.write(("*3\r\n$8\r\nREPLCONF\r\n$4\r\ncapa\r\n$6\r\npsync2\r\n").getBytes());
        out.flush();
        String resp2 = in.readLine();
    }

    public ReplicaHandler(ReplicationInformation replicationInformation, String[] args) {
        this.replicationInformation = replicationInformation;
        this.args = args;
    }

    public void listenForCommands(RedisContext redisContext) throws IOException {
        InputStream in = masterSocket.getInputStream();
        OutputStream out = masterSocket.getOutputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        dto.StringReader stringReader = new StringReader(bufferedReader);
        String redisVersion = stringReader.read();
        while(true) {
            String line = bufferedReader.readLine();
            LOG.info("line: " + line);
            if(line==null) break;
            line = stringReader.read();
            HashMap<String, Value> map = redisContext.getMap();
            if("SET".equals(line)) {
                map.put(stringReader.read(), new Value(stringReader.read(), -1L));
            }
            else if("DEL".equals(line)) {
                if(!map.containsKey(stringReader.read())) out.write("$-1\r\n".getBytes());
                map.remove(stringReader.read());
            }
            else if("GET".equals(line)) {
                String key = stringReader.read();
                if(!map.containsKey(key)) out.write("$-1\r\n".getBytes());
                Value v = map.get(key);
                out.write(("$" + v.getValue().length() + "\r\n" + v.getValue() + "\r\n").getBytes());
            }
            else if("REPLCONF".equals(line)) {
                out.write(("*3\r\n$8\r\nREPLCONF\r\n$3\r\nACK\r\n$1\r\n0\r\n").getBytes());
            }
        }
    }
}
