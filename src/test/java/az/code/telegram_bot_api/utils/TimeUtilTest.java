package az.code.telegram_bot_api.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class TimeUtilTest {
    TimeUtil timeUtil;
    static MockedStatic<Clock> clockMockedStatic;

    @BeforeAll
    static void cloak() {
        clockMockedStatic = Mockito.mockStatic(Clock.class, CALLS_REAL_METHODS);
    }

    @BeforeEach
    void setData() {
        timeUtil = new TimeUtil();
        timeUtil.startTime = "09:00";
        timeUtil.endTime = "17:00";
        timeUtil.responseLimit = "00:45";
        timeUtil.setData();
    }

    @Test
    @DisplayName("TimeUtilTest - Add time limit - Valid")
    void addLimit() {
        assertEquals(LocalTime.parse("20:45:00"), timeUtil.addLimit(LocalTime.parse("20:00:00")));
    }

    @Test
    @DisplayName("TimeUtilTest - is now work time - Valid")
    void isWorkTime() {
        mockClock("2021-12-22T15:15:30.00Z");
        assertTrue(isWorkTimeBool());
    }


    @Test
    @DisplayName("TimeUtilTest - is now work time - Valid")
    void isWorkTimeOut() {
        mockClock("2021-12-22T05:15:30.00Z");
        assertFalse(isWorkTimeBool());
    }

    @Test
    @DisplayName("TimeUtilTest - get time reminder - Valid")
    void getTimeReminder() {
        LocalTime localTime = LocalTime.parse("16:41", timeUtil.dtf);
        LocalTime expected = LocalTime.parse("09:26", timeUtil.dtf);
        assertEquals(expected, timeUtil.getTimeReminder(localTime));
    }

    @Test
    @DisplayName("TimeUtilTest - is Within Range false- Valid")
    void isWithinRange() {
        LocalTime localTime = LocalTime.parse("16:01", timeUtil.dtf);
        assertFalse(timeUtil.isWithinRange(localTime));
    }

    @Test
    @DisplayName("TimeUtilTest - is Within Range true- Valid")
    void isWithinRangeTrue() {
        LocalTime localTime = LocalTime.parse("06:01", timeUtil.dtf);
        assertTrue(timeUtil.isWithinRange(localTime));
    }

    @Test
    @DisplayName("TimeUtilTest - convert LocalDate and LocalTime to LocalDateTime- Valid")
    void convertToLocalDateTime() {
        LocalTime localTime = LocalTime.parse("06:01", timeUtil.dtf);
        LocalDate localDate = LocalDate.parse("2021-12-12");
        assertEquals(LocalDateTime.of(localDate, localTime), timeUtil.convertToLocalDateTime(localDate, localTime));
    }

    @Test
    @DisplayName("TimeUtilTest - get Expire Time If Time Before Work Time- Valid")
    void getExpireTimeIfTimeBeforeWorkTime() {
        mockClock("2021-12-22T05:15:30.00Z");
        assertEquals(LocalDateTime.parse("2021-12-22T09:45:00.00"), timeUtil.getExpireTime());
    }

    @Test
    @DisplayName("TimeUtilTest - get Expire Time If Time After Work Time - Valid")
    void getExpireTimeIfTimeAfterWorkTime() {
        mockClock("2021-12-22T18:15:30.00Z");
        assertEquals(LocalDateTime.parse("2021-12-23T09:45:00.00"), timeUtil.getExpireTime());
    }

    @Test
    @DisplayName("TimeUtilTest - get Expire Time If Time before Work - Valid")
    void getIfTimeBeforeWorkTimeButExpire() {
        mockClock("2021-12-22T16:45:00.00Z");
        assertEquals(LocalDateTime.parse("2021-12-23T09:30:00.00"), timeUtil.getExpireTime());
    }

    @Test
    @DisplayName("TimeUtilTest - get Expire Time If Time IN Work Time- Valid")
    void getIfTimeBeforeInWorkTime() {
        mockClock("2021-12-22T16:00:00.00Z");
        assertEquals(LocalDateTime.parse("2021-12-22T16:45:00.00"), timeUtil.getExpireTime());
    }


    private boolean isWorkTimeBool() {
        try {
            timeUtil.isWorkTime();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void mockClock(String val) {
        Clock clock = Clock.fixed(Instant.parse(val), ZoneId.of("UTC"));
        clockMockedStatic.when(Clock::systemDefaultZone).thenReturn(clock);
    }
}