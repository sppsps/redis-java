package commands;

import dto.LockObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public interface IListCommand {
    public void execute(BufferedReader reader, ConcurrentHashMap<String, List<String>> l, OutputStream out, int numArgs, ConcurrentHashMap<String, LockObject> lockMap) throws IOException, InterruptedException;
}
