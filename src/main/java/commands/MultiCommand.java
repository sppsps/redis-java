package commands;

import dto.StringReader;
import dto.Transaction;
import dto.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

public class MultiCommand implements ISetGetCommand{

    @Override
    public void execute(BufferedReader reader, HashMap<String, Value> map, OutputStream out, List<Transaction> transactionList, boolean isMultiActive, String cmd, List<String > keyVal) throws IOException {
        out.write(("+OK\r\n").getBytes());
out.flush();
    }
}
