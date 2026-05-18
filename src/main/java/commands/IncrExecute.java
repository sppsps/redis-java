package commands;

import dto.StringReader;
import dto.Transaction;
import dto.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

public class IncrExecute implements Execute{
    @Override
    public void execute(BufferedReader reader, HashMap<String, Value> map, OutputStream out, List<Transaction> transactionList, String cmd) throws IOException {
        StringReader stringReader = new StringReader(reader);
        String key = stringReader.read();
        String response = process(key, map, null);
        out.write(response.getBytes());
    }
    public String process(String key, HashMap<String, Value> map, Value nullValue) {
        Value val = new Value("0", -1L);
        StringBuilder s = new StringBuilder();
        if(map.containsKey(key)) {
            val = map.get(key);
        }
        String value = val.getValue();
        String newVal = "";
        try {
            newVal = addOneToString(value);
        } catch (Exception ex) {
            s.append(("-ERR value is not an integer or out of range\r\n"));
            return s.toString();
        }
        val.setValue(newVal);
        map.put(key, val);
        s.append(":").append(newVal).append("\r\n");
        return s.toString();
    }
    public String addOneToString(String value) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        int carry = 0;
        int idx = value.length()-1;
        while(idx>=0) {
            int f = value.charAt(idx)-48;
            if(f<0 || f>9) throw new Exception("error");
            if(idx==value.length()-1) f = f+carry+1;
            else f = f+carry;
            carry = f/10;
            f = f%10;
            stringBuilder.append(f);
            idx--;
        }
        if(carry!=0) stringBuilder.append(carry);
        stringBuilder.reverse();
        return stringBuilder.toString();
    }
}
