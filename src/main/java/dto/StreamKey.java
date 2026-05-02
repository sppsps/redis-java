package dto;

public class StreamKey {
    String millisTime;
    String seqNum;

    @Override
    public String toString() {
        return millisTime+"-"+seqNum;
    }
    public StreamKey(String millisTime, String seqNum) {
        this.seqNum = seqNum;
        this.millisTime = millisTime;
    }

    public String getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(String seqNum) {
        this.seqNum = seqNum;
    }

    public String getMillisTime() {
        return millisTime;
    }

    public void setMillisTime(String millisTime) {
        this.millisTime = millisTime;
    }
}
