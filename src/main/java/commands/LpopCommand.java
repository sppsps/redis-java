package commands;

import dto.StringReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LpopCommand implements IListCommand{
    private Logger log = LogManager.getLogger(LpopCommand.class);
    @Override
    public void execute(BufferedReader bufferedReader, HashMap<String, List<String>> listMap, OutputStream out, int numArgs) throws IOException {
        StringReader reader = new StringReader(bufferedReader);
        log.info("numArgs: "+ numArgs);
        String command = reader.read();
        log.info("Command: "+command);
        List<String> s = listMap.getOrDefault(command, new ArrayList<>());
        String val = "";
        if(s.size()==0) {
            out.write("$-1\r\n".getBytes());
            return;
        }

        String first = s.getFirst();
        s.removeFirst();
        log.info("completed");
        out.write(("$" + first.length() + "\r\n" + first + "\r\n").getBytes());
    }
}
