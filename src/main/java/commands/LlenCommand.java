package commands;

import dto.LockObject;
import dto.StringReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class LlenCommand implements IListCommand{
    @Override
    public void execute(BufferedReader bufferedReader, ConcurrentHashMap<String, List<String>> listMap, OutputStream out, int numArgs, ConcurrentHashMap<String, LockObject> lockMap) throws IOException {
        StringReader reader = new StringReader(bufferedReader);
        String listKey = reader.read();
        List<String> s = listMap.getOrDefault(listKey, new ArrayList<>());
        out.write((":"+s.size()+"\r\n").getBytes());
    }
}
