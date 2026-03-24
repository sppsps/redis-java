package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
public class Value {
    private String value;
    private Long timeToExpire;

    public Value(String value, Long timeToExpire) {
        this.value = value;
        this.timeToExpire = timeToExpire;
    }

    public Long getTimeToExpire() {
        return timeToExpire;
    }

    public void setTimeToExpire(Long timeToExpire) {
        this.timeToExpire = timeToExpire;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
