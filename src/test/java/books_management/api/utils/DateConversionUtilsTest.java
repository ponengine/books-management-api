package books_management.api.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DateConversionUtilsTest {

    @Test
    void convertToLocalDate() {

        String dateString = "2568-10-01";
        LocalDate expected = LocalDate.of(2025, 10, 1);
        LocalDate actual = DateConversionUtils.convertBuddhistToIso(dateString);
        assertEquals(expected, actual);
    }

    @Test
    void convertToLocalDateWithInvalidYear() {
        String invalidYearString = "2399-10-01";
        assertThrows(IllegalArgumentException.class, () -> {
            DateConversionUtils.convertBuddhistToIso(invalidYearString);
        });
    }

    @Test
    void convertToLocalDateWithInvalidFormat() {
        String invalidFormatString = "2568/10/01";
        assertThrows(IllegalArgumentException.class, () -> {
            DateConversionUtils.convertBuddhistToIso(invalidFormatString);
        });
    }


    @Test
    void convertToLocalDateWithNullInput() {
        String nullString = null;
        assertThrows(NullPointerException.class, () -> {
            DateConversionUtils.convertBuddhistToIso(nullString);
        });
    }

}