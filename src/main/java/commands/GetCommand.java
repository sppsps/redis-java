package commands;

import dto.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class GetCommand implements ISetGetCommand{
    @Override
    public void execute(BufferedReader bufferedReader, HashMap<String, Value>map, OutputStream out) throws IOException {
        String queryKeyChars = bufferedReader.readLine();
        String queryKey = bufferedReader.readLine();
        long curTime = System.currentTimeMillis();
        Value ans = map.getOrDefault(queryKey, new Value("-1", -1L));
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
