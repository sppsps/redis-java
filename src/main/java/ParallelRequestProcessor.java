import commands.*;
import dto.StringReader;
import dto.Value;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Setter
@Getter
public class ParallelRequestProcessor implements Runnable {
    private InputStream in;
    private OutputStream out;
    private HashMap<String, Value> map = new HashMap<>();
    HashMap<String, List<String>> listMap = new HashMap<>();
    private List<String> l = new ArrayList<>();
    private final Logger LOG = LogManager.getLogger(ParallelRequestProcessor.class);

    @Override
    public void run() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringReader reader = new StringReader(bufferedReader);
        String numArgs = "";
        try {
            numArgs = bufferedReader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int args = Integer.parseInt(numArgs.substring(1));
        while(true){
            try {
                String line = bufferedReader.readLine();
                LOG.info(line);
                if(line==null) break;
                if(line.startsWith("*")) args = Integer.parseInt(line.substring(1));
                if("PING".equals(line)) {
                    out.write("+PONG\r\n".getBytes());
                    out.flush();
                }
                else if("ECHO".equals(line)) {
                    ICommand command = new EchoCommand();
                    command.execute(bufferedReader, line, out);
                }
                else if("SET".equals(line)) {
                    ISetGetCommand setCommand = new SetCommand();
                    setCommand.execute(bufferedReader, map, out);
                }
                else if("GET".equals(line)) {
                    ISetGetCommand getCommand = new GetCommand();
                    getCommand.execute(bufferedReader, map, out);
                }
                else if("RPUSH".equals(line)) {
                    IListCommand listCommand = new RPushCommand();
                    listCommand.execute(bufferedReader, listMap, out, args-2);
                }
                else if("LRANGE".equals(line)) {
                    IListCommand lrangeCommand = new LRangeCommand();
                    lrangeCommand.execute(bufferedReader, listMap, out, args);
                }
                else if("LPUSH".equals(line)) {
                    IListCommand lpush = new LPushCommand();
                    lpush.execute(bufferedReader, listMap, out, args-2);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ParallelRequestProcessor(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }
}
