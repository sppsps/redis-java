package commands;

import dto.ReplicationInformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

public interface ReplicationCommand {
    void execute(BufferedReader reader, OutputStream out, ReplicationInformation replicationInformation) throws IOException;
}
