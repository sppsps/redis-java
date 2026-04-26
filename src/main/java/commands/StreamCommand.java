package commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface StreamCommand {
    void execute(BufferedReader bufferedReader, ConcurrentHashMap<String, HashMap<String, String>> map, OutputStream out, int args) throws IOException;
}
