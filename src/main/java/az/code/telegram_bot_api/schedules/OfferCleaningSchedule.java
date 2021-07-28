package az.code.telegram_bot_api.schedules;

import az.code.telegram_bot_api.repositories.UserRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class OfferCleaningSchedule {
    final
    UserRequestRepository requestRepo;

    public OfferCleaningSchedule(UserRequestRepository requestRepo) {
        this.requestRepo = requestRepo;
    }

    //todo x days
//    @Scheduled(cron = "${cron.offerCleaningExpression}", zone = "Asia/Baku")
//    public void removeDeletedRequest() {
//        List<UserRequest> requestList = requestRepo.getDeleteItems();
//        log.info("removeDeletedRequest Scheduler started working. Requests count :" + requestList.size());
//        requestRepo.deleteAll(requestList);
//    }
}
