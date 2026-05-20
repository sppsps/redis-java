package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ReplicationInformation {
    boolean isReplica;
    String host;
    String port;
    String replicationId;
    String replicationOffset;

    public String getReplicationId() {
        return replicationId;
    }

    public void setReplicationId(String replicationId) {
        this.replicationId = replicationId;
    }

    public String getReplicationOffset() {
        return replicationOffset;
    }

    public void setReplicationOffset(String replicationOffset) {
        this.replicationOffset = replicationOffset;
    }

    public ReplicationInformation() {
    }

    public ReplicationInformation(String host, boolean isReplica, String port) {
        this.host = host;
        this.isReplica = isReplica;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isReplica() {
        return isReplica;
    }

    public void setReplica(boolean replica) {
        isReplica = replica;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
