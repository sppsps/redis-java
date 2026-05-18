package commands;

import dto.StringReader;
import dto.Transaction;
import dto.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

public class SetExecutor implements Execute{
    @Override
    public void execute(BufferedReader bufferedReader, HashMap<String, Value> map, OutputStream out, List<Transaction> transactionList, String cmd) throws IOException {
        StringReader reader = new StringReader(bufferedReader);
        String keyChars = bufferedReader.readLine();
        String key = bufferedReader.readLine();
        String valChars = bufferedReader.readLine();
        String val = bufferedReader.readLine();
        out.write("+OK\r\n".getBytes());
        String px = reader.read();
        Value value = new Value(val, -1L);
        if(px.equals("PX") || px.equals("EX")) {
            String timeToExpire = reader.read();
            long curTime = System.currentTimeMillis();
            value.setTimeToExpire(px.equals("PX")?Long.parseLong(timeToExpire)+curTime
                    :curTime+1000*Long.parseLong(timeToExpire));
        }

        process(key, map, value);
    }

    @Override
    public String process(String key, HashMap<String, Value> map, Value val) {
        map.put(key, val);
        return "+OK\r\n";
    }

}
