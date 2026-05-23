package commands;

import dto.LockObject;
import dto.StreamKey;
import dto.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class XReadCommand implements StreamCommand{
    private final Logger Log = LogManager.getLogger(XReadCommand.class);
    @Override
    public void execute(BufferedReader reader, ConcurrentHashMap<StreamKey, HashMap<String, String>> map, OutputStream out, int args, HashMap<String, List<StreamKey>> keyMap, ConcurrentHashMap<String, LockObject> lockMap) throws IOException, InterruptedException {
        StringReader stringReader = new StringReader(reader);
        String readType = stringReader.read();
        String blockTime = null;
        if(readType.equalsIgnoreCase("BLOCK")) {
            blockTime = stringReader.read();
            readType = stringReader.read();
            args-=2;
        }
        List<String>listKeys = new ArrayList<>();
        List<String> startIds = new ArrayList<>();
        for(int i=0;i<args;i++) {
            if(i<args/2) listKeys.add(stringReader.read());
            else startIds.add(stringReader.read());
        }
        if(blockTime==null)  out.write(("*"+listKeys.size()+"\r\n").getBytes());
out.flush();
        for(int i=0;i<listKeys.size();i++) {
            StreamKey latestKey = keyMap.getOrDefault(listKeys.get(i), new ArrayList<>()).getLast();
            if(startIds.get(i).equals("$")) processReads(out, listKeys.get(i), latestKey.toString(), keyMap, map, blockTime, lockMap, listKeys.size());
            else processReads(out, listKeys.get(i), startIds.get(i), keyMap, map, blockTime, lockMap, listKeys.size());
        }
    }

    private void processReads(OutputStream out, String listKey, String startId, HashMap<String, List<StreamKey>> keyMap, ConcurrentHashMap<StreamKey, HashMap<String, String>> map, String blockTime, ConcurrentHashMap<String, LockObject> lockMap, int size) throws IOException, InterruptedException {
        List<StreamKey> keys = keyMap.getOrDefault(listKey, new ArrayList<>());
        List<StreamKey> keysInRange = new ArrayList<>();
        if(!lockMap.containsKey(listKey)) lockMap.put(listKey, new LockObject());
        LockObject lockObject = lockMap.get(listKey);
        String[] startIds = new String[2];
        startIds = startId.split("-");
//        if(startId.equals("$")) {
//            startIds[0] = keys==null || keys.isEmpty()? "0":keys.getLast().getMillisTime();
//            startIds[1] = keys==null || keys.isEmpty()? "0":keys.getLast().getSeqNum();
////            startId = startIds[0]+"-"+startIds[1];
//        }
        if(blockTime!=null) {
            Lock lock = lockObject.getLock();
            Condition condition = lockObject.getCondition();
            lock.lock();
            long initialTime = System.currentTimeMillis();
            try {
                if(blockTime.equals("0")) {
                    while(keys!=null && keys.isEmpty() || !keys.getLast().compareStrictlyIdsMore(startIds)) {
                        condition.await();
                        keys = keyMap.getOrDefault(listKey, new ArrayList<>());
                    }
                }
                else {
                    while (keys.isEmpty() || !keys.getLast().compareStrictlyIdsMore(startIds)) {
                        condition.awaitNanos(Long.parseLong(blockTime) * 1_1000);
                        keys = keyMap.getOrDefault(listKey, new ArrayList<>());
                        if ((System.currentTimeMillis() - initialTime > Long.parseLong(blockTime))) break;
                    }
                }
            } finally {
                lock.unlock();
                keys = keyMap.getOrDefault(listKey, new ArrayList<>());
            }
            if(keys==null || keys.isEmpty()) {
                Log.info("HEY");
                out.write("*-1\r\n".getBytes());
out.flush();
                return;
            }
            String[] finalStartIds1 = startIds;
            keys.forEach((key) -> {
                if (key.inRange(startId.equals("$")? finalStartIds1[0]+"-"+ finalStartIds1[1]:startId, "+")) {
                    if(!(key.getMillisTime().equals(finalStartIds1[0]) && key.getSeqNum().equals(finalStartIds1[1]))) keysInRange.add(key);
                }
            });
        }
        else {
            String[] finalStartIds = startIds;
            keys.forEach((key) -> {
                if (key.inRange(startId.equals("$")? finalStartIds[0]+"-"+ finalStartIds[1]:startId, "+")) keysInRange.add(key);
            });
        }
        if(keysInRange.size()==0) {
            out.write("*-1\r\n".getBytes());
out.flush();
            return;
        }
        if(blockTime!=null) out.write(("*"+size+"\r\n").getBytes());
out.flush();
        out.write(("*"+"2"+"\r\n").getBytes());
out.flush();
        out.write(("$"+listKey.length()+"\r\n"+listKey+"\r\n").getBytes());
out.flush();
        out.write(("*" + keysInRange.size() + "\r\n").getBytes());
out.flush();
        keysInRange.forEach((key)->{
            HashMap<String, String> keyVals = map.get(key);
            try {
                out.write(("*"+"2"+"\r\n").getBytes());
out.flush();
                out.write(("$"+key.length()+"\r\n"+key+"\r\n").getBytes());
out.flush();
                out.write(("*"+keyVals.size()*2+"\r\n").getBytes());
out.flush();
                keyVals.forEach((k, v)->{
                    try {
                        out.write(("$"+k.length()+"\r\n"+k+"\r\n").getBytes());
out.flush();
                        out.write(("$"+v.length()+"\r\n"+v+"\r\n").getBytes());
out.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
