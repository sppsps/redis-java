package commands;

import dto.StringReader;
import dto.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class IncrCommand implements ISetGetCommand{
    @Override
    public void execute(BufferedReader reader, HashMap<String, Value> map, OutputStream out) throws IOException {
        StringReader stringReader = new StringReader(reader);
        String key = stringReader.read();
        Value val = map.get(key);
        String value = val.getValue();
        String newVal = addOneToString(value);
        val.setValue(newVal);
        map.put(key, val);
        out.write((":"+newVal+"\r\n").getBytes());
    }

    private String addOneToString(String value) {
        StringBuilder stringBuilder = new StringBuilder();
        int carry = 0;
        int idx = value.length()-1;
        while(idx>=0) {
            int f = value.charAt(idx)-48;
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
