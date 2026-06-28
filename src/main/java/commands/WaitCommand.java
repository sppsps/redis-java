package commands;

import dto.Replicas;
import dto.ReplicationInformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

public class WaitCommand{
    public void execute(BufferedReader reader, OutputStream out, Replicas replicas) throws IOException {
        int numReplicas = replicas.getReplicationInformationList().size();
        out.write((":"+numReplicas+"\r\n").getBytes());
    }
}
