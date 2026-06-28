package commands;

import dto.ReplicationInformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

public class WaitCommand implements ReplicationCommand{
    @Override
    public void execute(BufferedReader reader, OutputStream out, ReplicationInformation replicationInformation) throws IOException {
        out.write(":0\r\n".getBytes());
    }
}
