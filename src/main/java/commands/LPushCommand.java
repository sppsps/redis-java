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

public class LPushCommand implements IListCommand{
    private Logger log = LogManager.getLogger(LPushCommand.class);
    @Override
    public void execute(BufferedReader bufferedReader, ConcurrentHashMap<String, List<String>> listMap, OutputStream out, int numArgs, ConcurrentHashMap<String, LockObject> lockMap) throws IOException {
        StringReader reader = new StringReader(bufferedReader);
        log.info("numArgs: "+ numArgs);
        String command = reader.read();
        log.info("Command: "+command);
        List<String> s = listMap.getOrDefault(command, new ArrayList<>());
        String val = "";
        while(numArgs>0) {
            val = reader.read();
            log.info("val " + val+" numargs "+numArgs);
            s.addFirst(val);
            log.info("Size: "+s.size());
            numArgs--;
        }
        listMap.put(command, s);
        log.info("completed");
        out.write((":" + s.size() + "\r\n").getBytes());
    }
}
