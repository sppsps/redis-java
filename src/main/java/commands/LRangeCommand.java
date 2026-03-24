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

public class LRangeCommand implements IListCommand{
    private Logger log = LogManager.getLogger(LRangeCommand.class);
    @Override
    public void execute(BufferedReader bufferedReader, HashMap<String, List<String>> listMap, OutputStream out, int numArgs) throws IOException {
        StringReader reader = new StringReader(bufferedReader);
        String command = reader.read();

        List<String> s = listMap.getOrDefault(command, new ArrayList<>());
        int start = Integer.parseInt(reader.read());
        int stop = Math.min(Integer.parseInt(reader.read()), s.size()-1);
        log.info("Start: "+start+"Stop: "+stop);
        log.info("ListSize: "+s.size());
        if(!listMap.containsKey(command) || start>stop || start>s.size()-1) {
            out.write("*0\r\n".getBytes());
            return;
        }
        int numElements = stop-start+1;
        log.info("numele: "+numElements);
        StringBuilder sb = new StringBuilder();
        sb.append("*").append(numElements).append("\r\n");
        for(;start<=stop;start++) {
            sb.append("$").append(s.get(start).length()).append("\r\n").append(s.get(start)).append("\r\n");
        }
//        sb.append("\r\n");
        out.write(sb.toString().getBytes());
    }
}
