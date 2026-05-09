package commands;

import dto.StreamKey;
import dto.StringReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class XReadCommand implements StreamCommand{
    @Override
    public void execute(BufferedReader reader, ConcurrentHashMap<StreamKey, HashMap<String, String>> map, OutputStream out, int args, HashMap<String, List<StreamKey>> keyMap) throws IOException {
        StringReader stringReader = new StringReader(reader);
        String readType = stringReader.read();
        String listKey = stringReader.read();
        String startId = stringReader.read();
        List<StreamKey> keys = keyMap.get(listKey);
        List<StreamKey> keysInRange = new ArrayList<>();
        keys.forEach((key)->{
            if(key.inRange(startId, "+")) keysInRange.add(key);
        });
        out.write(("*"+keysInRange.size()+"\r\n").getBytes());
        out.write(("*"+"2"+"\r\n").getBytes());
        out.write(("$"+listKey.length()+"\r\n"+listKey+"\r\n").getBytes());
        out.write(("*" + keysInRange.size() + "\r\n").getBytes());
        keysInRange.forEach((key)->{
            HashMap<String, String> keyVals = map.get(key);
            try {
                out.write(("*"+"2"+"\r\n").getBytes());
                out.write(("$"+key.length()+"\r\n"+key+"\r\n").getBytes());
                out.write(("*"+keyVals.size()*2+"\r\n").getBytes());
                keyVals.forEach((k, v)->{
                    try {
                        out.write(("$"+k.length()+"\r\n"+k+"\r\n").getBytes());
                        out.write(("$"+v.length()+"\r\n"+v+"\r\n").getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
