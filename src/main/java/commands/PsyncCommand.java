package commands;

import dto.ReplicationInformation;
import dto.StringReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Base64;

public class PsyncCommand implements ReplicationCommand{
    @Override
    public void execute(BufferedReader reader, OutputStream out, ReplicationInformation replicationInformation) throws IOException {
        StringReader stringReader = new StringReader(reader);
        String replicationId = stringReader.read();
        String replicaOffset = stringReader.read();
        out.write("+FULLRESYNC 8371b4fb1155b71f4a04d3e1bc3e18c4a990aeeb 0\r\n".getBytes());
out.flush();
        String emptyRDBFile = new String("UkVESVMwMDEx+glyZWRpcy12ZXIFNy4yLjD6CnJlZGlzLWJpdHPAQPoFY3RpbWXCbQi8ZfoIdXNlZC1tZW3CsMQQAPoIYW9mLWJhc2XAAP/wbjv+wP9aog==");
        byte[] rdbFileBytes = Base64.getDecoder().decode(emptyRDBFile);
        out.write(("$"+rdbFileBytes.length+"\r\n").getBytes());
out.flush();
        out.write(rdbFileBytes);
out.flush();
        replicationInformation.setOut(out);
    }
}
