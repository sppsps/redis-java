package commands;

import dto.StreamKey;
import dto.StringReader;
import dto.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TypeCommand implements ITypeCommand{
    @Override
    public void execute(BufferedReader bufferedReader, HashMap<String, Value> map, HashMap<String, List<StreamKey>> streamMap, OutputStream out) throws IOException {
        StringReader reader = new StringReader(bufferedReader);
        String key = reader.read();
        if(map.containsKey(key)) {
            out.write(("+string\r\n").getBytes());
        }
        else if(streamMap.containsKey(key)) {
            out.write(("+stream\r\n").getBytes());
        }
        else out.write("+none\r\n".getBytes());
    }
}
