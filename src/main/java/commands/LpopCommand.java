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

public class LpopCommand implements IListCommand{
    private Logger log = LogManager.getLogger(LpopCommand.class);
    @Override
    public void execute(BufferedReader bufferedReader, ConcurrentHashMap<String, List<String>> listMap, OutputStream out, int numArgs, ConcurrentHashMap<String, LockObject> lockMap) throws IOException {
        StringReader reader = new StringReader(bufferedReader);
        log.info("numArgs: "+ numArgs);
        String command = reader.read();
        log.info("Command: "+command);
        List<String> s = listMap.getOrDefault(command, new ArrayList<>());
        log.info("Size of list: "+ s.size());
        String val = "";
        if(s.size()==0) {
            out.write("$-1\r\n".getBytes());
            return;
        }
        int numRemoves = 1;

        if (numArgs == 3) {
            String countStr = reader.read();
            numRemoves = Integer.parseInt(countStr);
        }
        log.info("Number of removals: " + numRemoves);
        List<String>removed = new ArrayList<>();
        while(numRemoves>0) {
            String first = s.getFirst();
            removed.add(first);
            s.removeFirst();
            numRemoves--;
        }
        log.info("Removed array "+ removed);
        if(removed.size()==1) {
            out.write(("$"+removed.getFirst().length()+"\r\n"+removed.getFirst()+"\r\n").getBytes());
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("*").append(removed.size()).append("\r\n");
        for(String element: removed) {
                sb.append("$").append(element.length()).append("\r\n").append(element).append("\r\n");
        };
        log.info("stringbuilder: " + sb.toString());
        out.write(sb.toString().getBytes());
        out.flush();
    }
}
