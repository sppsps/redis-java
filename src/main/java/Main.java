import context.RedisContext;
import dto.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
  public static void main(String[] args) throws IOException {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");
    //  Uncomment the code below to pass the first stage
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 6379;
        boolean isReplica = false;
        String[] master = new String[2];
        ReplicationInformation replicationInformation = new ReplicationInformation();
        if(args.length>0 && args[0].equals("--port")) port = Integer.parseInt(args[1]);
        if(args.length>3) {
            isReplica = args[2].equals("--replicaof");
            replicationInformation.setReplica(isReplica);
            master = args[3].split(" ");;
            replicationInformation.setHost(master[0]);
            replicationInformation.setPort(master[1]);
        }
        try {
          serverSocket = new ServerSocket(port);
          // Since the tester restarts your program quite often, setting SO_REUSEADDR
          // ensures that we don't run into 'Address already in use' errors
          serverSocket.setReuseAddress(true);
          // Wait for connection from client.
            RedisContext redisContext = new RedisContext(replicationInformation);

          while(true){
                clientSocket = serverSocket.accept();
                if(clientSocket==null) break;
                DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
                ParallelRequestProcessor requestProcessor = new ParallelRequestProcessor(inputStream, outputStream, redisContext, new ArrayList<>());
                Thread thread = new Thread(requestProcessor);
                thread.start();
          }
        } catch (IOException e) {
          System.out.println("IOException: " + e.getMessage());
        } finally {
          try {
            if (clientSocket != null) {
                clientSocket.close();
            }
          } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
          }
        }
  }
}
