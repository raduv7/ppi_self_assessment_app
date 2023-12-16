package sas.business.util.generator;

import java.sql.Timestamp;
import java.time.Instant;

public class TimestampGenerator {
    public static Timestamp now() {             // with microsecond precision
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        timestamp.setNanos(Instant.now().getNano());
        return timestamp;
    }
}
