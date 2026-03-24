package commands;

import dto.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public interface ISetGetCommand {
    public void execute(BufferedReader reader, HashMap<String, Value> map, OutputStream out) throws IOException;
}
