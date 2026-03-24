package commands;

import dto.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RPushCommand implements IListCommand{
    private Logger log = LogManager.getLogger(RPushCommand.class);
    @Override
    public void execute(BufferedReader bufferedReader, HashMap<String, List<String>> listMap, OutputStream out, int numArgs) throws IOException {
        StringReader reader = new StringReader(bufferedReader);
        log.info("numArgs: "+ numArgs);
        String command = reader.read();
        log.info("Command: "+command);
        List<String> s = listMap.getOrDefault(command, new ArrayList<>());
        String val = "";
        while(numArgs>0) {
            val = reader.read();
            log.info("val " + val+" numargs "+numArgs);
            s.add(val);
            log.info("Size: "+s.size());
            numArgs--;
        }
        listMap.put(command, s);
        log.info("completed");
        out.write((":" + s.size() + "\r\n").getBytes());
    }
}
