package az.code.telegram_bot_api.utils;

import az.code.telegram_bot_api.exceptions.NotWorkTimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
public class TimeUtil {
    @Value("${timer.start}")
    String startTime;
    @Value("${timer.end}")
    String endTime;
    @Value("${timer.responseLimit}")
    String responseLimit;

    LocalTime start;
    LocalTime end;
    LocalTime limit;
    DateTimeFormatter dtf;


    public LocalDateTime getExpireTime() {
        setData();
        LocalTime now = LocalTime.parse(LocalTime.now().format(dtf));
        LocalTime expireTime = addLimit(now);
        LocalDateTime time = LocalDateTime.now();
        if (isWithinRange(now) || isWithinRange(expireTime)) {
            if (now.isBefore(start)) {
                time = convertToLocalDateTime(LocalDate.now(), addLimit(start));
            } else if (now.isAfter(end) || now == end) {
                time = convertToLocalDateTime(LocalDate.now().plusDays(1), addLimit(start));
            } else if (expireTime.isAfter(end)) {
                time = convertToLocalDateTime(LocalDate.now().plusDays(1), getTimeReminder(now));
            }
        } else {
            time = convertToLocalDateTime(LocalDate.now(), expireTime);
        }

        return time;
    }

    public void setData() {
        dtf = DateTimeFormatter.ofPattern("HH:mm");
        start = LocalTime.parse(this.startTime, dtf);
        end = LocalTime.parse(this.endTime, dtf);
        limit = LocalTime.parse(this.responseLimit, dtf);
    }

    public LocalDateTime convertToLocalDateTime(LocalDate now, LocalTime localTime) {
        return LocalDateTime.of(now, localTime);
    }

    boolean isWithinRange(LocalTime date) {
        return date.isBefore(start) || date.isAfter(end);
    }

    public LocalTime getTimeReminder(LocalTime now) {
        long diffHour = now.until(end, ChronoUnit.HOURS);
        long diffMinute = now.until(end, ChronoUnit.MINUTES) % 60;
        return start.plusHours(diffHour).plusMinutes(limit.getHour()*60+limit.getMinute()-diffMinute);
    }

    public void isWorkTime() {
        setData();
        if (isWithinRange(LocalTime.now())) {
            throw new NotWorkTimeException();
        }
    }

    public LocalTime addLimit(LocalTime time) {
        return time.plusHours(limit.getHour()).plusMinutes(limit.getMinute());
    }
}
