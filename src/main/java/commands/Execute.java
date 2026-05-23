package commands;

import dto.Transaction;
import dto.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

public interface Execute {
    void execute(BufferedReader reader, HashMap<String, Value> map, OutputStream out, List<Transaction> transactionList, String cmd, List<String> keyVals) throws IOException;
    String process(String key, HashMap<String, Value>map, Value val);
}
