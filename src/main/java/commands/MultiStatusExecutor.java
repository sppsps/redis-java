package commands;

import dto.StringReader;
import dto.Transaction;
import dto.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

public class MultiStatusExecutor implements Execute{
    @Override
    public void execute(BufferedReader reader, HashMap<String, Value> map, OutputStream out, List<Transaction> transactionList, String command) throws IOException {
        out.write("+QUEUED\r\n".getBytes());
        StringReader stringReader = new StringReader(reader);
        String key = stringReader.read();
        String val = stringReader.read();
        Transaction t = new Transaction(command, key, val);
        transactionList.add(t);
    }

    @Override
    public String process(String key, HashMap<String, Value> map, Value val) {
        return "";
    }
}
