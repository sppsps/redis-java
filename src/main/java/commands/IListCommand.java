package commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface IListCommand {
    public void execute(BufferedReader reader, List<String> s, OutputStream out) throws IOException;
}
