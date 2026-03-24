package dto;

import java.io.BufferedReader;
import java.io.IOException;

public class StringReader {
    public BufferedReader reader;

    public StringReader(BufferedReader bufferedReader) {
        this.reader = bufferedReader;
    }

    public String read() throws IOException {
        String line = reader.readLine();
        if(line==null) return null;
        line = reader.readLine();
        return line;
    }
}
