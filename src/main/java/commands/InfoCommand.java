package commands;

import dto.ReplicationInformation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

public class InfoCommand implements ReplicationCommand{

    @Override
    public void execute(BufferedReader reader, OutputStream out, ReplicationInformation replicationInformation) throws IOException {
        String isReplica = replicationInformation.isReplica()?"slave":"master";
        int totalsize = replicationInformation.isReplica()?84:85;
        out.write(("$" + totalsize+ "\r\n" + "role:" + isReplica +"master_replid:8371b4fb1155b71f4a04d3e1bc3e18c4a990aeeb" +"master_repl_offset:0" + "\r\n").getBytes());
out.flush();
//        out.write(("$54\r\n"+"master_replid:8371b4fb1155b71f4a04d3e1bc3e18c4a990aeeb").getBytes());
out.flush();
//        out.write(("$18\r\n"+"master_repl_offset:0\r\n").getBytes());
out.flush();
    }
}
