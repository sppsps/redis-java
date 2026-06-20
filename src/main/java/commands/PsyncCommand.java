package commands;

import dto.Replicas;
import dto.ReplicationInformation;
import dto.StringReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Base64;

public class PsyncCommand{

    public void execute(BufferedReader reader, OutputStream out, Replicas replicas) throws IOException {
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
        ReplicationInformation replicationInformation = new ReplicationInformation();
        replicationInformation.setOut(out);
        replicas.addToReplicas(replicationInformation);
    }
}
