package dto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;

public class StringReader {
    public BufferedReader reader;
    private Logger log = LogManager.getLogger(StringReader.class);

    public StringReader(BufferedReader bufferedReader) {
        this.reader = bufferedReader;
    }

    public String read() {
        String line = null;
        try {
            if(!reader.ready()) return null;
            line = reader.readLine();
        } catch (IOException e) {
            return null;
        }
        log.info("Line 1: "+line);
        if(line==null) return null;

        try {
            line = reader.readLine();
        } catch (IOException e) {
            return null;
        }
        log.info("line2: "+line);
        return line;
    }
}
