package commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;

public interface ICommand {
    void execute(BufferedReader reader, String line, OutputStream outputStream) throws IOException;
}
