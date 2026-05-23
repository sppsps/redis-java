package commands;

import dto.Transaction;
import dto.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiscardCommand implements ISetGetCommand{

    @Override
    public void execute(BufferedReader reader, HashMap<String, Value> map, OutputStream out, List<Transaction> transactionList, boolean isMultiActive, String cmd, List<String > keyVal) throws IOException {
        if(!isMultiActive) {
            out.write("-ERR DISCARD without MULTI\r\n".getBytes());
out.flush();
        }
        else {
            transactionList.clear();
            out.write("+OK\r\n".getBytes());
out.flush();
        }
    }
}
