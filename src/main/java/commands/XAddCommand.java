package commands;

import com.sun.source.doctree.EscapeTree;
import dto.StreamKey;
import dto.StringReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class XAddCommand implements StreamCommand{
    @Override
    public void execute(BufferedReader reader, ConcurrentHashMap<StreamKey, HashMap<String, String>> map, OutputStream out, int args, HashMap<String, List<StreamKey>> keyList) throws IOException {
        StringReader stringReader = new StringReader(reader);
        String listKey = stringReader.read();
        String id = stringReader.read();
        String[] key = id.split("-");
        StreamKey streamKey = new StreamKey(key[0], key[1]);
        List<StreamKey> streamIdList = keyList.getOrDefault(listKey, new ArrayList<>());
        if(key[1].equals("*")) {
            autoIncrement(streamKey, keyList.get(listKey));
        }
        HashMap<String, String> mapList = new HashMap<>();

        if(!validateId(out, map, streamKey, streamIdList)) {
            return;
        }
        else {
            streamIdList.add(streamKey);
            keyList.put(listKey, streamIdList);
        }
        while(args>0) {
            String arg = stringReader.read();
            args--;
            String val = stringReader.read();
            args--;
            mapList.put(arg, val);
        }
        map.put(streamKey, mapList);
        out.write(("$"+id.length()+"\r\n"+streamKey.toString()+"\r\n").getBytes());
    }

    private void autoIncrement(StreamKey streamKey, List<StreamKey> streamKeys) {
        String millis = streamKey.getMillisTime();
        StreamKey latestKey = null;
        if(streamKeys==null) {
            if(streamKey.getMillisTime().equals("0")) streamKey.setSeqNum("1");
            else streamKey.setSeqNum("0");
            return;
        }
        for (StreamKey key : streamKeys) {
            if(key.getMillisTime().equals(millis)) latestKey = key;
        }
        if(latestKey==null) {
            if(streamKey.getMillisTime().equals("0")) streamKey.setSeqNum("1");
            else streamKey.setSeqNum("0");
        }
        else streamKey.setSeqNum(String.valueOf(Integer.parseInt(latestKey.getSeqNum()) + 1));
    }

    private boolean validateId(OutputStream out, ConcurrentHashMap<StreamKey, HashMap<String, String>> map, StreamKey curKey, List<StreamKey> st) throws IOException {
        if(st==null || st.isEmpty()) {
            if(curKey.getMillisTime().equals("0")) {
                if(curKey.getSeqNum().compareTo("0")>0) {
                    return true;
                }
                else {
                    out.write("-ERR The ID specified in XADD must be greater than 0-0\r\n".getBytes());
                    return false;
                }
            }
            else return true;
        }
        StreamKey topKey = st.getLast();
        if(curKey.getMillisTime().equals("0") && curKey.getSeqNum().equals("0")) {
            out.write("-ERR The ID specified in XADD must be greater than 0-0\r\n".getBytes());
            return false;
        }
        if(curKey.getMillisTime().compareTo(topKey.getMillisTime())>0) {
            return true;
        }
        else if(curKey.getMillisTime().compareTo(topKey.getMillisTime())==0) {
            if(curKey.getSeqNum().compareTo(topKey.getSeqNum())>0) return true;
            else {
                out.write("-ERR The ID specified in XADD is equal or smaller than the target stream top item\r\n".getBytes());
                return false;
            }
        }
        else {
            out.write("-ERR The ID specified in XADD is equal or smaller than the target stream top item\r\n".getBytes());
            return false;
        }
//        st.add(new StreamKey(topKey.getMillisTime(), topKey.getSeqNum()));
    }
}
