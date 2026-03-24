import dto.StringReader;
import dto.Value;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;


@Setter
@Getter
public class ParallelRequestProcessor implements Runnable {
    private InputStream in;
    private OutputStream out;
    private HashMap<String, Value> map = new HashMap<>();
    private final Logger LOG = LogManager.getLogger(ParallelRequestProcessor.class);

    @Override
    public void run() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringReader reader = new StringReader(bufferedReader);
        while(true){
            try {
                String line = bufferedReader.readLine();
                LOG.info(line);
                if(line==null) break;
                if("PING".equals(line)) {
                    out.write("+PONG\r\n".getBytes());
                    out.flush();
                }
                else if("ECHO".equals(line)) {
                    while(line!=null) {
                        line = bufferedReader.readLine();
                        out.write((line+"\r\n").getBytes());
                        LOG.info(line);
                    }
                }
                else if("SET".equals(line)) {
                    String keyChars = bufferedReader.readLine();
                    String key = bufferedReader.readLine();
                    String valChars = bufferedReader.readLine();
                    String val = bufferedReader.readLine();
                    LOG.info("key: "+key + " "+ "value: "+val);
                    out.write("+OK\r\n".getBytes());
                    String px = reader.read();
                    Value value = new Value(val, -1L);
                    LOG.info("px: "+ px);
                    if(px.equals("PX") || px.equals("EX")) {
                        String timeToExpire = reader.read();
                        LOG.info(timeToExpire);
                        long curTime = System.currentTimeMillis();
                        LOG.info("cur time: "+curTime);
                        value.setTimeToExpire(px.equals("PX")?Long.parseLong(timeToExpire)+curTime
                                :curTime+1000*Long.parseLong(timeToExpire));
                        LOG.info("expiry time: "+value.getTimeToExpire());
                    }
                    map.put(key, value);

                }
                else if("GET".equals(line)) {
                    String queryKeyChars = bufferedReader.readLine();
                    String queryKey = bufferedReader.readLine();
                    long curTime = System.currentTimeMillis();
                    Value ans = map.getOrDefault(queryKey, new Value("-1", -1L));
                    if(ans.getTimeToExpire()==-1)
                    {
                        out.write(("$"+ans.getValue().length()+"\r\n"+ans.getValue()+"\r\n").getBytes());
                        continue;
                    }
                    if("-1".equals(ans.getValue()) || curTime>ans.getTimeToExpire()) {
                        out.write("$-1\r\n".getBytes());
                        continue;
                    }
                    out.write(("$"+ans.getValue().length()+"\r\n"+ans.getValue()+"\r\n").getBytes());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ParallelRequestProcessor(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }
}
