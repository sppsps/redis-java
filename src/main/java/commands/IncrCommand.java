package commands;

import dto.StringReader;
import dto.Transaction;
import dto.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

public class IncrCommand implements ISetGetCommand{
    @Override
    public void execute(BufferedReader reader, HashMap<String, Value> map, OutputStream out, List<Transaction> transactionList, boolean isMultiActive) throws IOException {
        if(!isMultiActive){
            Execute executor = new IncrExecute();
            executor.execute(reader, map, out, transactionList);
        }
        else {
            Execute executor = new MultiStatusExecutor();
            executor.execute(reader, map, out, transactionList);
        }
    }
}
