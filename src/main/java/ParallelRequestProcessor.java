import commands.*;
import context.RedisContext;
import dto.*;
import dto.StringReader;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Setter
@Getter
public class ParallelRequestProcessor implements Runnable {
    private final ConcurrentHashMap<StreamKey, HashMap<String, String>> streamMap;
    private final List<Transaction> transactions;
    private InputStream in;
    private OutputStream out;
    ConcurrentHashMap<String, LockObject> lockMap;
    private HashMap<String, Value> map;
    ConcurrentHashMap<String, List<String>> listMap;
    private List<String> l = new ArrayList<>();
    private final Logger LOG = LogManager.getLogger(ParallelRequestProcessor.class);
    HashMap<String, List<StreamKey>> streamKeyIdMap;
    boolean isMultiActive = false;
    ReplicationInformation replicationInformation;
    AtomicBoolean isReplicationActive;

    @Override
    public void run() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringReader reader = new StringReader(bufferedReader);
        String numArgs = "";
        List<String> keyVals = new ArrayList<>();

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
                if(line.startsWith("-p")) {
                    reader.read();
                }
                if(line.startsWith("*")) args = Integer.parseInt(line.substring(1));
                if("PING".equals(line)) {
                    out.write("+PONG\r\n".getBytes());
out.flush();
                    out.flush();
                }
                else if("ECHO".equals(line)) {
                    ICommand command = new EchoCommand();
                    command.execute(bufferedReader, line, out);
                }
                else if("SET".equals(line)) {
                    ISetGetCommand setCommand = new SetCommand();
                    keyVals = new ArrayList<>();
                    LOG.info("Started set ");
                    setCommand.execute(bufferedReader, map, out, transactions, isMultiActive, "", keyVals);
                    if(isReplicationActive.get()) sendCommandToReplica(out, replicationInformation, keyVals);
                }
                else if("GET".equals(line)) {
                    ISetGetCommand getCommand = new GetCommand();
                    getCommand.execute(bufferedReader, map, out, transactions, isMultiActive, "", keyVals);
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
                    typeCommand.execute(bufferedReader, map, streamKeyIdMap, out);
                }
                else if("BLPOP".equals(line)) {
                    IListCommand blpop = new BLpopCommand();
                    blpop.execute(bufferedReader, listMap, out, args, lockMap);
                }
                else if("XADD".equals(line)) {
                    StreamCommand xAddCommand = new XAddCommand();
                    xAddCommand.execute(bufferedReader, streamMap, out, args-3, streamKeyIdMap, lockMap);
                }
                else if("XRANGE".equals(line)) {
                    StreamCommand xrangeCommand = new XRangeCommand();
                    xrangeCommand.execute(bufferedReader, streamMap, out, args-3, streamKeyIdMap, lockMap);
                }
                else if("XREAD".equals(line)) {
                    StreamCommand xreadCommand = new XReadCommand();
                    xreadCommand.execute(bufferedReader, streamMap, out, args-2, streamKeyIdMap, lockMap);
                }
                else if("INCR".equals(line)) {
                    ISetGetCommand incrCommand = new IncrCommand();
                    incrCommand.execute(bufferedReader, map, out, transactions, isMultiActive, "", keyVals);
                }
                else if("MULTI".equals(line)) {
                    isMultiActive = true;
                    ISetGetCommand multiCommand = new MultiCommand();
                    multiCommand.execute(bufferedReader, map, out, transactions, isMultiActive, "", keyVals);
                }
                else if("EXEC".equals(line)) {
                    ISetGetCommand execCommand = new ExecCommand();
                    execCommand.execute(bufferedReader, map, out, transactions, isMultiActive, "", keyVals);
                    isMultiActive = false;
                }
                else if("DISCARD".equals(line)) {
                    ISetGetCommand discardCommand = new DiscardCommand();
                    discardCommand.execute(bufferedReader, map, out, transactions, isMultiActive, "", keyVals);
                    isMultiActive = false;
                }
                else if("INFO".equals(line)) {
                    ReplicationCommand infoCommand = new InfoCommand();
                    infoCommand.execute(bufferedReader, out, replicationInformation);
                }
                else if("REPLCONF".equals(line)) {
                    ReplicationCommand replicationCommand = new ReplicationConfig();
                    replicationCommand.execute(bufferedReader, out, replicationInformation);
                }
                else if("PSYNC".equals(line)) {
                    ReplicationCommand psyncCommand = new PsyncCommand();
                    psyncCommand.execute(bufferedReader, out, replicationInformation);
                    isReplicationActive.set(true);
                }
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendCommandToReplica(OutputStream out, ReplicationInformation replicationInformation, List<String> keyVals) throws IOException {
        String key = keyVals.get(0);
        String value = keyVals.get(1);
        LOG.info("PROPAGATING " + key);

        replicationInformation.getOut().write(("*3\r\n"+"$3\r\n"+"SET\r\n"+"$"+key.length()+"\r\n"+key+"\r\n"+"$"+value.length()+"\r\n"+value+"\r\n").getBytes());
//        System.out.println("FLUSHED " + key);
        LOG.info("FLUSHED "+key);
        replicationInformation.getOut().flush();
    }

    public ParallelRequestProcessor(InputStream in, OutputStream out, RedisContext redisContext, List<Transaction> transactions, AtomicBoolean isReplicationActive) {
        this.in = in;
        this.out = out;
        this.map = redisContext.getMap();
        this.lockMap = redisContext.getLockMap();
        this.listMap = redisContext.getListMap();
        this.streamMap = redisContext.getStreamMap();
        this.streamKeyIdMap = redisContext.getStreamKeyMap();
        this.replicationInformation = redisContext.getReplicationInformation();
        this.transactions = transactions;
        this.isReplicationActive = isReplicationActive;
    }
}
