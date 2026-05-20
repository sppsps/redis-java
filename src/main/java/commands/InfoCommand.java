package commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

public class InfoCommand implements ReplicationCommand{

    @Override
    public void execute(BufferedReader reader, OutputStream out) throws IOException {
        out.write(("$"+"11"+"\r\n"+"role:master\r\n").getBytes());
    }
}
