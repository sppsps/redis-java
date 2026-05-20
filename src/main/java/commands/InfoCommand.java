package commands;

import dto.ReplicationInformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

public class InfoCommand implements ReplicationCommand{

    @Override
    public void execute(BufferedReader reader, OutputStream out, ReplicationInformation replicationInformation) throws IOException {
        if(!replicationInformation.isReplica()){
            out.write(("$" + "11" + "\r\n" + "role:master\r\n").getBytes());
        }
        else {
            out.write(("$" + "10" + "\r\n" + "role:slave\r\n").getBytes());
        }
    }
}
