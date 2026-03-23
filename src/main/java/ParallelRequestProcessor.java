import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


@Setter
@Getter
public class ParallelRequestProcessor implements Runnable {
    private InputStream in;
    private OutputStream out;
    private HashMap<String, String> map = new HashMap<>();
    private final Logger LOG = LogManager.getLogger(ParallelRequestProcessor.class);
    @Override
    public void run() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        while(true){
            try {
                String line = bufferedReader.readLine();
                LOG.info(line);
                if(line==null) break;
                if("PING".equals(line)) {
                    out.write("+PONG\r\n".getBytes());
                    out.flush();
                }
                else if("ECHO".equals(line)) {
                    while(line!=null) {
                        line = bufferedReader.readLine();
                        out.write((line+"\r\n").getBytes());
                        LOG.info(line);
                    }
                }
                else if("SET".equals(line)) {
                    String keyChars = bufferedReader.readLine();
                    String key = bufferedReader.readLine();
                    String valChars = bufferedReader.readLine();
                    String val = bufferedReader.readLine();
                    map.put(key, val);
                    LOG.info("key: "+key + " "+ "value: "+val);
                    out.write("+OK\r\n".getBytes());
                }
                else if("GET".equals(line)) {

                    String queryKeyChars = bufferedReader.readLine();
                    LOG.info("QKC: "+queryKeyChars);
                    String queryKey = bufferedReader.readLine();
                    LOG.info("querykey: "+queryKey+" "+"value: "+map.getOrDefault(queryKey, "-1"));
                    String ans = map.getOrDefault(queryKey, "-1");
                    if("-1".equals(ans)) {
                        out.write("$-1\r\n".getBytes());
                        continue;
                    }
                    out.write(("$"+ans.length()+"\r\n"+ans+"\r\n").getBytes());
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
