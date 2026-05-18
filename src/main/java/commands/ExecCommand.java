package commands;

import dto.Transaction;
import dto.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

public class ExecCommand implements ISetGetCommand{
    @Override
    public void execute(BufferedReader reader, HashMap<String, Value> map, OutputStream out, List<Transaction> transactionList, boolean isMultiActive) throws IOException {
        System.out.println(isMultiActive);
        if(!isMultiActive) {
            out.write("-ERR EXEC without MULTI\r\n".getBytes());
        }
    }
}
