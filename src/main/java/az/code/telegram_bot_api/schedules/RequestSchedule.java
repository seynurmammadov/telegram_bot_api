package az.code.telegram_bot_api.schedules;

import az.code.telegram_bot_api.models.Request;
import az.code.telegram_bot_api.models.UserRequest;
import az.code.telegram_bot_api.models.enums.RequestStatus;
import az.code.telegram_bot_api.repositories.RequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class RequestSchedule {
    final
    RequestRepository requestRepository;

    public RequestSchedule(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Scheduled(cron = "${cron.expression}", zone = "Asia/Baku")
    public void expiredRequests() {
        List<Request> requests = requestRepository.getAllActive();
        LocalDateTime nowDate = LocalDateTime.now();
        log.info("expiredRequests Scheduler started working. Requests count :" + requests.size());
        for (Request r : requests) {
            if (r.getExperationDate().isBefore(nowDate)) {
                for (UserRequest userRequest : r.getUserRequests()) {
                    userRequest.setArchived(true);
                    userRequest.setRequestStatus(RequestStatus.EXPIRED);
                }
                r.setExpired(true);
                requestRepository.save(r);
            }
        }
    }
}
