package commands;

import dto.Transaction;
import dto.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

public class GetExecutor implements Execute{
    @Override
    public void execute(BufferedReader reader, HashMap<String, Value> map, OutputStream out, List<Transaction> transactionList, String cmd) throws IOException {
        String queryKeyChars = reader.readLine();
        String queryKey = reader.readLine();
        out.write(process(queryKey, map, null).getBytes());
    }
    @Override
    public String process(String queryKey, HashMap<String, Value> map, Value value) {
        long curTime = System.currentTimeMillis();
        StringBuilder s = new StringBuilder();
        Value ans = map.getOrDefault(queryKey, new Value("-1", -1L));
        if(!map.containsKey(queryKey)) {
            s.append("$-1\r\n");
            return s.toString();
        }
        if(ans.getTimeToExpire()==-1)
        {
            s.append("$").append(ans.getValue().length()).append("\r\n").append(ans.getValue()).append("\r\n");
            return s.toString();
        }
        if("-1".equals(ans.getValue()) || curTime>ans.getTimeToExpire()) {
            s.append("$-1\r\n");
            return s.toString();
        }
        s.append("$").append(ans.getValue().length()).append("\r\n").append(ans.getValue()).append("\r\n");
        return s.toString();
    }

}
