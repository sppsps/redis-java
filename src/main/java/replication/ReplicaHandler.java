package replication;

import dto.ReplicationInformation;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ReplicaHandler {
    private String[] args;
    ReplicationInformation replicationInformation;
    Socket masterSocket;
    public void handlePings() throws Exception {
        boolean isReplica = args[2].equals("--replicaof");
        replicationInformation.setReplica(isReplica);
        String[] master = args[3].split(" ");;
        replicationInformation.setHost(master[0]);
        replicationInformation.setMasterPort(Integer.parseInt(master[1]));
        System.out.println(replicationInformation.getHost());
        System.out.println(replicationInformation.getPort());
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
}
