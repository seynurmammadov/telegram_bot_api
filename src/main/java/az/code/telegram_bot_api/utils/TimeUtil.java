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
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = addLimit(now);
        if (isWithinRange(now.toLocalTime()) || isWithinRange(expireTime.toLocalTime())) {
            if (now.toLocalTime().isBefore(start)) {
                now = convertToLocalDateTime(LocalDate.now(), addLimit(start));
            } else if (now.toLocalTime().isAfter(end) || now.toLocalTime() == end) {
                now = convertToLocalDateTime(LocalDate.now().plusDays(1), addLimit(start));
            } else if (expireTime.toLocalTime().isAfter(end)) {
                now = convertToLocalDateTime(LocalDate.now().plusDays(1), getTimeReminder(now.toLocalTime()));
            }
        } else {
            now = expireTime;
        }
        return now;
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
        return start.plusHours(diffHour).plusMinutes(limit.getHour() * 60 + limit.getMinute() - diffMinute);
    }

    public void isWorkTime() {
        setData();
        if (isWithinRange(LocalTime.now())) {
            throw new NotWorkTimeException();
        }
    }

    public LocalDateTime addLimit(LocalDateTime time) {
        return time.plusHours(limit.getHour()).plusMinutes(limit.getMinute());
    }

    public LocalTime addLimit(LocalTime time) {
        return time.plusHours(limit.getHour()).plusMinutes(limit.getMinute());
    }
}
