package commands;

import dto.StringReader;
import dto.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class TypeCommand implements ISetGetCommand{
    @Override
    public void execute(BufferedReader bufferedReader, HashMap<String, Value> map, OutputStream out) throws IOException {
        StringReader reader = new StringReader(bufferedReader);
        String key = reader.read();
        if(map.containsKey(key)) {
            out.write(("+string\r\n").getBytes());
        }
        else out.write("+none\r\n".getBytes());
    }
}
