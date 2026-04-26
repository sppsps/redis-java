import commands.*;
import dto.Commands;
import dto.LockObject;
import dto.StringReader;
import dto.Value;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Setter
@Getter
public class ParallelRequestProcessor implements Runnable {
    private final ConcurrentHashMap<String, HashMap<String, String>> streamMap;
    private InputStream in;
    private OutputStream out;
    ConcurrentHashMap<String, LockObject> lockMap;
    private HashMap<String, Value> map = new HashMap<>();
    ConcurrentHashMap<String, List<String>> listMap;
    private List<String> l = new ArrayList<>();
    private final Logger LOG = LogManager.getLogger(ParallelRequestProcessor.class);

    @Override
    public void run() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringReader reader = new StringReader(bufferedReader);
        String numArgs = "";

        try {
            numArgs = bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int args = Integer.parseInt(numArgs.substring(1));
        while(true){
            try {
                String line = bufferedReader.readLine();
                LOG.info(line);
                if(line==null) break;
                if(line.startsWith("*")) args = Integer.parseInt(line.substring(1));
                if("PING".equals(line)) {
                    out.write("+PONG\r\n".getBytes());
                    out.flush();
                }
                else if("ECHO".equals(line)) {
                    ICommand command = new EchoCommand();
                    command.execute(bufferedReader, line, out);
                }
                else if("SET".equals(line)) {
                    ISetGetCommand setCommand = new SetCommand();
                    setCommand.execute(bufferedReader, map, out);
                }
                else if("GET".equals(line)) {
                    ISetGetCommand getCommand = new GetCommand();
                    getCommand.execute(bufferedReader, map, out);
                }
                else if("RPUSH".equals(line)) {
                    IListCommand listCommand = new RPushCommand();
                    listCommand.execute(bufferedReader, listMap, out, args-2, lockMap);
                }
                else if("LRANGE".equals(line)) {
                    IListCommand lrangeCommand = new LRangeCommand();
                    lrangeCommand.execute(bufferedReader, listMap, out, args, lockMap);
                }
                else if("LPUSH".equals(line)) {
                    IListCommand lpush = new LPushCommand();
                    lpush.execute(bufferedReader, listMap, out, args-2, lockMap);
                }
                else if("LLEN".equals(line)) {
                    IListCommand llen = new LlenCommand();
                    llen.execute(bufferedReader, listMap, out, args, lockMap);
                }
                else if("LPOP".equals(line)) {
                    IListCommand lpop = new LpopCommand();
                    lpop.execute(bufferedReader, listMap, out, args, lockMap);
                }
                else if("TYPE".equals(line)) {
                    ITypeCommand typeCommand = new TypeCommand();
                    typeCommand.execute(bufferedReader, map, streamMap, out);
                }
                else if("BLPOP".equals(line)) {
                    IListCommand blpop = new BLpopCommand();
                    blpop.execute(bufferedReader, listMap, out, args, lockMap);
                }
                else if("XADD".equals(line)) {
                    StreamCommand streamCommand = new XAddCommand();
                    streamCommand.execute(bufferedReader, streamMap, out, args-3);
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ParallelRequestProcessor(InputStream in, OutputStream out, ConcurrentHashMap<String, LockObject> lockMap, ConcurrentHashMap<String, List<String>> listMap, ConcurrentHashMap<String, HashMap<String, String>> streamMap) {
        this.in = in;
        this.out = out;
        this.lockMap = lockMap;
        this.listMap = listMap;
        this.streamMap = streamMap;
    }
}
