package dto;

import java.util.ArrayList;
import java.util.List;

public class Replicas {
    List<ReplicationInformation> replicationInformationList = new ArrayList();

    public Replicas() {
    }

    public Replicas(List<ReplicationInformation> replicationInformationList) {
        this.replicationInformationList = replicationInformationList;
    }

    public void addToReplicas(ReplicationInformation replicationInformation) {
        this.replicationInformationList.add(replicationInformation);
    }

    public List<ReplicationInformation> getReplicationInformationList() {
        return replicationInformationList;
    }

    public void setReplicationInformationList(List<ReplicationInformation> replicationInformationList) {
        this.replicationInformationList = replicationInformationList;
    }
}
