package luiz.imageRepo.utils;

import com.sun.istack.NotNull;

import java.sql.Timestamp;
import java.time.Instant;

public class Functions {

    public static String generateRemoteName(@NotNull String originalName) {
        String timeStampInNanoSeconds = String.valueOf(Timestamp.from(Instant.now()).getNanos());
        String UUID = java.util.UUID.randomUUID().toString();
        return timeStampInNanoSeconds.concat("_" + UUID).concat("_" + originalName);
    }
}
