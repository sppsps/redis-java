package dto;

public enum Commands {
    ECHO,
    PING,
    SET,
    GET,
    RPUSH,
    LRANGE,
    LPUSH,
    LLEN,
    LPOP;

    public static Commands from(String input) {
        return Commands.valueOf(input);
    }
}