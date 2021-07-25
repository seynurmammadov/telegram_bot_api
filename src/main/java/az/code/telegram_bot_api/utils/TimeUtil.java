package az.code.telegram_bot_api.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
    int limit;
    DateTimeFormatter dtf;



    public LocalDateTime getExpireTime() {
        setData();
        LocalTime now = LocalTime.parse(LocalTime.now().format(dtf));
        LocalTime expireTime =now.plusHours(limit);
        LocalDateTime time = LocalDateTime.now();
        if (isWithinRange(now) || isWithinRange(expireTime)) {
            if (now.isBefore(start)) {
                time = getTime(LocalDate.now(), start.plusHours(limit));
            }
            if (now.isAfter(end) || now == end) {
                time = getTime(LocalDate.now().plusDays(1), start.plusHours(limit));
            }
        } else {
            time = getTime(LocalDate.now(), expireTime);
        }
        return time;
    }

    private void setData() {
        dtf = DateTimeFormatter.ofPattern("HH:mm");
        start = LocalTime.parse(this.startTime, dtf);
        end = LocalTime.parse(this.endTime, dtf);
        limit = Integer.parseInt(responseLimit);
    }

    private LocalDateTime getTime(LocalDate now, LocalTime localTime) {
        return LocalDateTime.of(now, localTime);
    }

    boolean isWithinRange(LocalTime date) {
        return date.isBefore(start) || date.isAfter(end);
    }
}
