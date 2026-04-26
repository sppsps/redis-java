package commands;

import dto.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public interface ITypeCommand {
    void execute(BufferedReader bufferedReader, HashMap<String, Value> map, ConcurrentHashMap<String, HashMap<String, String>>streamMap, OutputStream out) throws IOException;
}
