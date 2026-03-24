package commands;

import dto.StringReader;
import dto.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class SetCommand implements ISetGetCommand {
    private Logger LOG = LogManager.getLogger(SetCommand.class);
    public OutputStream out;
    @Override
    public void execute(BufferedReader bufferedReader, HashMap<String, Value>map, OutputStream out) throws IOException{
        StringReader reader = new StringReader(bufferedReader);
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
}
