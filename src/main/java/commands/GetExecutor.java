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
    public void execute(BufferedReader reader, HashMap<String, Value> map, OutputStream out, List<Transaction> transactionList) throws IOException {
        String queryKeyChars = reader.readLine();
        String queryKey = reader.readLine();
        long curTime = System.currentTimeMillis();
        Value ans = map.getOrDefault(queryKey, new Value("-1", -1L));
        if(!map.containsKey(queryKey)) {
            out.write("$-1\r\n".getBytes());
            return;
        }
        if(ans.getTimeToExpire()==-1)
        {
            out.write(("$"+ans.getValue().length()+"\r\n"+ans.getValue()+"\r\n").getBytes());
            return ;
        }
        if("-1".equals(ans.getValue()) || curTime>ans.getTimeToExpire()) {
            out.write("$-1\r\n".getBytes());
            return;
        }
        out.write(("$"+ans.getValue().length()+"\r\n"+ans.getValue()+"\r\n").getBytes());
    }
}
