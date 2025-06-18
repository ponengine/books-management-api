package books_management.api.utils;

import java.time.LocalDate;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.chrono.ThaiBuddhistDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class DateConversionUtils {
    public static LocalDate convertBuddhistToIso(String buddhistDateString) {
        if (buddhistDateString == null || buddhistDateString.isEmpty()) {
            throw new NullPointerException("Input date string cannot be null or empty");
        }
        String[] parts = buddhistDateString.split("-");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Date must be in the format yyyy-MM-dd");
        }
        int year = Integer.parseInt(parts[0]);
        if (year < 2400) {
            throw new IllegalArgumentException("Year must be in the Thai Buddhist calendar (e.g. 2568)");
        }

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd")
                .toFormatter()
                .withChronology(ThaiBuddhistChronology.INSTANCE);

        ThaiBuddhistDate thaiDate = ThaiBuddhistDate.from(formatter.parse(buddhistDateString));
        return LocalDate.from(thaiDate);
    }
}
