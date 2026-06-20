package context;

import dto.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@NoArgsConstructor
public class RedisContext {
    ConcurrentHashMap<String, LockObject> lockMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, List<String>> listMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<StreamKey, HashMap<String, String>> streamMap = new ConcurrentHashMap<>();
    HashMap<String, List<StreamKey>> streamKeyMap = new HashMap<>();
    HashMap<String, Value>map = new HashMap<>();
    ReplicationInformation replicationInformation = new ReplicationInformation();
    Replicas replicas = new Replicas();

    public Replicas getReplicas() {
        return replicas;
    }

    public void setReplicas(Replicas replicas) {
        this.replicas = replicas;
    }

    public RedisContext(ReplicationInformation replicationInformation) {
        this.replicationInformation = replicationInformation;
    }

    public ConcurrentHashMap<String, LockObject> getLockMap() {
        return lockMap;
    }

    public void setLockMap(ConcurrentHashMap<String, LockObject> lockMap) {
        this.lockMap = lockMap;
    }

    public ConcurrentHashMap<String, List<String>> getListMap() {
        return listMap;
    }

    public void setListMap(ConcurrentHashMap<String, List<String>> listMap) {
        this.listMap = listMap;
    }

    public ConcurrentHashMap<StreamKey, HashMap<String, String>> getStreamMap() {
        return streamMap;
    }

    public void setStreamMap(ConcurrentHashMap<StreamKey, HashMap<String, String>> streamMap) {
        this.streamMap = streamMap;
    }

    public HashMap<String, List<StreamKey>> getStreamKeyMap() {
        return streamKeyMap;
    }

    public void setStreamKeyMap(HashMap<String, List<StreamKey>> streamKeyMap) {
        this.streamKeyMap = streamKeyMap;
    }

    public HashMap<String, Value> getMap() {
        return map;
    }

    public void setMap(HashMap<String, Value> map) {
        this.map = map;
    }

    public ReplicationInformation getReplicationInformation() {
        return replicationInformation;
    }

    public void setReplicationInformation(ReplicationInformation replicationInformation) {
        this.replicationInformation = replicationInformation;
    }

    public RedisContext(ConcurrentHashMap<String, LockObject> lockMap, ConcurrentHashMap<String, List<String>> listMap, ConcurrentHashMap<StreamKey, HashMap<String, String>> streamMap, HashMap<String, List<StreamKey>> streamKeyMap, HashMap<String, Value> map, ReplicationInformation replicationInformation) {
        this.lockMap = lockMap;
        this.listMap = listMap;
        this.streamMap = streamMap;
        this.streamKeyMap = streamKeyMap;
        this.map = map;
        this.replicationInformation = replicationInformation;
    }
}
