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
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class RPushCommand implements IListCommand{
    private Logger log = LogManager.getLogger(RPushCommand.class);
    @Override
    public void execute(BufferedReader bufferedReader, ConcurrentHashMap<String, List<String>> listMap, OutputStream out, int numArgs, ConcurrentHashMap<String, LockObject> lockMap) throws IOException {
        StringReader reader = new StringReader(bufferedReader);
        log.info("numArgs: "+ numArgs);
        String command = reader.read();
        log.info("Command: "+command);
        if(!listMap.containsKey(command)) listMap.put(command, new ArrayList<>());
        List<String> s = listMap.get(command);
        String val = "";
        if(!lockMap.containsKey(command)) lockMap.put(command, new LockObject());
        LockObject lockObject = lockMap.get(command);
        Condition condition = lockObject.getCondition();
        Lock lock = lockObject.getLock();
        lock.lock();
        try {
            while (numArgs > 0) {
                val = reader.read();
                log.info("val " + val + " numargs " + numArgs);
                s.add(val);
                log.info("Size: " + s.size());
                numArgs--;
            }
            listMap.put(command, s);
            log.info("completed");
            out.write((":" + s.size() + "\r\n").getBytes());
        } finally {
            condition.signalAll();
            lock.unlock();
        }
    }
}
