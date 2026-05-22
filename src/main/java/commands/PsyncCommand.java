package commands;

import dto.ReplicationInformation;
import dto.StringReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

public class PsyncCommand implements ReplicationCommand{
    @Override
    public void execute(BufferedReader reader, OutputStream out, ReplicationInformation replicationInformation) throws IOException {
        StringReader stringReader = new StringReader(reader);
        String replicationId = stringReader.read();
        String replicaOffset = stringReader.read();
        out.write("+FULLRESYNC 8371b4fb1155b71f4a04d3e1bc3e18c4a990aeeb 0\r\n".getBytes());
    }
}
