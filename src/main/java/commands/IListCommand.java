package commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

public interface IListCommand {
    public void execute(BufferedReader reader, HashMap<String, List<String>> l, OutputStream out, int numArgs) throws IOException;
}
