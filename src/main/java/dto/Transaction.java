package dto;

public class Transaction {
    String command;
    String key;
    String value;

    public Transaction() {
    }

    public Transaction(String command, String key, String value) {
        this.command = command;
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
