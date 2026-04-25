package commands;

import dto.LockObject;
import dto.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BLpopCommand implements IListCommand {
    private final Logger log = LogManager.getLogger(BLpopCommand.class);
    @Override
    public void execute(BufferedReader reader, ConcurrentHashMap<String, List<String>> l, OutputStream out, int numArgs, ConcurrentHashMap<String, LockObject> lockMap) throws IOException, InterruptedException {
        StringReader stringReader = new StringReader(reader);
        String listKey = stringReader.read();
        String timeout = stringReader.read();
        if(!l.containsKey(listKey)) l.put(listKey, new ArrayList<>());
        List<String> list = l.get(listKey);
        String removed = "";
        if(!lockMap.containsKey(listKey)) lockMap.put(listKey, new LockObject());
        LockObject lockObject = lockMap.get(listKey);
        Condition condition = lockObject.getCondition();
        Lock lock = lockObject.getLock();
        lock.lock();
        try {
            double remaining = Double.parseDouble(timeout)*1000;
            if(!list.isEmpty()) {
                ;
            }
            else if (timeout.equals("0")) {
                    log.info("blpop 0 timeout");
                    condition.await(); // wait indefinitely
            } else {
                long start = System.currentTimeMillis();
                condition.awaitNanos((long)(remaining * 1_000_000));
                log.info("waited time: " + (long)(remaining * 1_000_000));
                if(l.get(listKey).isEmpty()) {
                    out.write("*-1\r\n".getBytes());
                    return;
                }
            }
            list = l.get(listKey);
            removed = list.removeFirst();
        } finally {
            lock.unlock();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("*").append(2).append("\r\n");
        sb.append("$").append(listKey.length()).append("\r\n").append(listKey).append("\r\n");
        sb.append("$").append(removed.length()).append("\r\n").append(removed).append("\r\n");
        out.write(sb.toString().getBytes());
        out.flush();
    }
}
