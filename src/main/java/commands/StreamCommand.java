package commands;

import dto.StreamKey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

public interface StreamCommand {
    void execute(BufferedReader bufferedReader, ConcurrentHashMap<StreamKey, HashMap<String, String>> map, OutputStream out, int args, HashMap<String, List<StreamKey>> st) throws IOException;
}
