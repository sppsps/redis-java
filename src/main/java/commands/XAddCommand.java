package commands;

import dto.StringReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class XAddCommand implements StreamCommand{
    @Override
    public void execute(BufferedReader reader, ConcurrentHashMap<String, HashMap<String, String>> map, OutputStream out, int args) throws IOException {
        StringReader stringReader = new StringReader(reader);
        String listKey = stringReader.read();
        String id = stringReader.read();

        HashMap<String, String> mapList = new HashMap<>();
        while(args>0) {
            String arg = stringReader.read();
            args--;
            String val = stringReader.read();
            args--;
            mapList.put(arg, val);
        }
        map.put(listKey, mapList);
        out.write(("$"+id.length()+"\r\n"+id+"\r\n").getBytes());
    }
}
