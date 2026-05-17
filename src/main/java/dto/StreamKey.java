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

    public int length() {
        return millisTime.length()+seqNum.length()+1;
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

    public boolean inRange(String startId, String endId) {
        String[] startIds = new String[2];
        String[] endIds = new String[2];
        boolean compare1 = false;
        boolean compare2 = false;
        if(startId.contains("-")) {
            if(startId.equals("-")) {
                compare1 = true;
            }
            else {
                startIds = startId.split("-");
                compare1 = compareIdsMore(startIds);
            }
        }
        else {
            compare1 = compareIdsMore(startId);
        }
        if(endId.contains("-")) {
            endIds = endId.split("-");
            compare2 = compareIdsLess(endIds);
        }
        else {
            if(endId.equals("+")) compare2 = true;
            else compare2 = compareIdsLess(endId);
        }
        return compare1 && compare2;
    }

    public boolean compareIdsMore(String[] Id) {
        if(millisTime.compareTo(Id[0])>=0 && seqNum.compareTo(Id[1])>=0) return true;
            else return false;
    }

    public boolean compareStrictlyIdsMore(String[] Id) {
        if(millisTime.compareTo(Id[0])>0 && seqNum.compareTo(Id[1])>=0) return true;
        else return millisTime.compareTo(Id[0]) == 0 && seqNum.compareTo(Id[1]) > 0;
    }

    private boolean compareIdsLess(String[] Id) {
        if(millisTime.compareTo(Id[0])<=0 && seqNum.compareTo(Id[1])<=0) return true;
        else return false;
    }

    private boolean compareIdsMore(String startId) {
        return millisTime.compareTo(startId) >= 0;
    }

    private boolean compareIdsLess(String Id) {
        return millisTime.compareTo(Id) <= 0;
    }
}
