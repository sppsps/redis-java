package commands;

import dto.StringReader;
import dto.Transaction;
import dto.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

public class SetCommand implements ISetGetCommand {
    private Logger LOG = LogManager.getLogger(SetCommand.class);
    public OutputStream out;
    @Override
    public void execute(BufferedReader bufferedReader, HashMap<String, Value>map, OutputStream out, List<Transaction> transactionList, boolean isMultiActive, String cmd, List<String > keyVal) throws IOException{
        if(!isMultiActive){
            Execute executor = new SetExecutor();
            executor.execute(bufferedReader, map, out, transactionList, "SET", keyVal);
        }
        else {
            Execute executor = new MultiStatusExecutor();
            executor.execute(bufferedReader, map, out, transactionList, "SET", keyVal);
        }
    }
}
