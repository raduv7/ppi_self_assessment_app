package sas.business.util.generator;

import java.util.UUID;
import org.apache.commons.codec.binary.Base32;

public class NumberGenerator {
    public static String generateUniqueNumber() {
        UUID uuid = UUID.randomUUID();
        byte[] bytes = new byte[16];
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();

        for (int i = 0; i < 8; i++) {
            bytes[i] = (byte) (msb >>> 8 * (7 - i));
            bytes[8 + i] = (byte) (lsb >>> 8 * (7 - i));
        }

        Base32 base32 = new Base32();
        return base32.encodeAsString(bytes).replace("=", "").toUpperCase();
    }
}

