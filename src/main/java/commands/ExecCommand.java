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
    public void execute(BufferedReader reader, HashMap<String, Value> map, OutputStream out, List<Transaction> transactionList, boolean isMultiActive, String cmd) throws IOException {
        System.out.println(isMultiActive);
        if(!isMultiActive) {
            out.write("-ERR EXEC without MULTI\r\n".getBytes());
        }
        else {
            if(transactionList==null || transactionList.isEmpty()) {
                out.write("*0\r\n".getBytes());
                return;
            }
            out.write(("*"+transactionList.size()+"\r\n").getBytes());
            transactionList.forEach((transaction -> {
                if(transaction.getCommand().equals("SET")) {
                    Execute executor = new SetExecutor();
                    String resp = executor.process(transaction.getKey(), map, new Value(transaction.getValue(), -1L));
                    try {
                        out.write(resp.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if(transaction.getCommand().equals("GET")) {
                    Execute executor = new GetExecutor();
                    String resp = executor.process(transaction.getKey(), map, new Value(transaction.getValue(), -1L));
                    try {
                        out.write(resp.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if(transaction.getCommand().equals("INCR")) {
                    Execute executor = new IncrExecute();
                    String resp = executor.process(transaction.getKey(), map, new Value(transaction.getValue(), -1L));
                    try {
                        out.write(resp.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }));
        }
    }
}
