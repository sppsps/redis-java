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
        if(start<-s.size()) start=0;
        if(stop<-s.size()) stop=0;
        if(start<0) start=s.size()+start;
        if(stop<0) stop = s.size()+stop;
        log.info("Start: "+start+"Stop: "+stop);
        log.info("ListSize: "+s.size());
        if(!listMap.containsKey(command) || start>stop || start>s.size()-1) {
            out.write("*0\r\n".getBytes());
            return;
        }
        List<String> queryList = s.subList(start, stop+1);
        int numElements = stop-start+1;
        log.info("numele: "+numElements);
        StringBuilder sb = new StringBuilder();
        sb.append("*").append(numElements).append("\r\n");
        queryList.forEach((element)->{
            sb.append("$").append(element.length()).append("\r\n").append(element).append("\r\n");
        });
//        sb.append("\r\n");
        out.write(sb.toString().getBytes());
    }
}
