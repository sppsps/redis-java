package commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

public class EchoCommand implements ICommand{
    @Override
    public void execute(BufferedReader reader, String line, OutputStream out) throws IOException {
        while(line!=null) {
            line = reader.readLine();
            out.write((line+"\r\n").getBytes());
        }
    }
}
