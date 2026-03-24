package commands;

import dto.StringReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class RPushCommand implements IListCommand{

    @Override
    public void execute(BufferedReader bufferedReader, List<String> s, OutputStream out) throws IOException {
        StringReader reader = new StringReader(bufferedReader);
        String command = reader.read();
        String val = reader.read();
        s.add(val);
        out.write((":"+ s.size() +"\r\n").getBytes());
    }
}
