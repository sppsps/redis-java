package commands;

import dto.Transaction;
import dto.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

public class GetCommand implements ISetGetCommand{
    @Override
    public void execute(BufferedReader bufferedReader, HashMap<String, Value>map, OutputStream out, List<Transaction> transactionList, boolean isMultiActive, String cmd) throws IOException {
        if(!isMultiActive) {
            Execute executor = new GetExecutor();
            executor.execute(bufferedReader, map, out, transactionList, "GET");
        }
        else {
            Execute executor = new MultiStatusExecutor();
            executor.execute(bufferedReader, map, out, transactionList, "GET");
        }
    }
}
