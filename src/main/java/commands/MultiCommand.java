package commands;

import dto.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class MultiCommand implements ISetGetCommand{

    @Override
    public void execute(BufferedReader reader, HashMap<String, Value> map, OutputStream out) throws IOException {
        out.write(("+OK\r\n").getBytes());
        return;
    }
}
