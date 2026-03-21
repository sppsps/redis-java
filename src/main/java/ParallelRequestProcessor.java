import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Setter
@Getter
public class ParallelRequestProcessor implements Runnable {
    private InputStream in;
    private OutputStream out;
    @Override
    public void run() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        while(true){
            try {
                String line = bufferedReader.readLine();
                if(line==null) break;
                if("PING".equals(line)) {
                    out.write("+PONG\r\n".getBytes());
                    out.flush();
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
