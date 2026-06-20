package dto;

import org.apache.logging.log4j.spi.CopyOnWrite;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ReplicationConnections {
    CopyOnWriteArrayList<Socket> socketList;
    List<DataOutputStream> outputStreams;

    public ReplicationConnections(CopyOnWriteArrayList<Socket> socketList, List<DataOutputStream> outputStreams) {
        this.socketList = socketList;
        this.outputStreams = outputStreams;
    }

    public ReplicationConnections() {

    }

    public List<Socket> getSocketList() {
        return socketList;
    }

    public void setSocketList(CopyOnWriteArrayList<Socket> socketList) {
        this.socketList = socketList;
    }

    public List<DataOutputStream> getOutputStreams() {
        return outputStreams;
    }

    public void setOutputStreams(List<DataOutputStream> outputStreams) {
        this.outputStreams = outputStreams;
    }
}
