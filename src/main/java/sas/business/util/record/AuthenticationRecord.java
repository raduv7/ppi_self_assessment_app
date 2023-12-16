package sas.business.util.record;

import sas.business.util.generator.TimestampGenerator;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticationRecord {
    private final Map<String, Timestamp> record;

    public AuthenticationRecord() {
        this.record = new HashMap<>();
    }

    public void setNewAction(String username) {
        record.put(username, TimestampGenerator.now());
    }

    public Timestamp getLastAction(String username) {
        return record.get(username);
    }

    public void removeLastAction(String username) {
        record.remove(username);
    }
}
