package az.code.telegram_bot_api.schedules;

import az.code.telegram_bot_api.configs.RabbitMQConfig;
import az.code.telegram_bot_api.models.AgencyOffer;
import az.code.telegram_bot_api.models.Request;
import az.code.telegram_bot_api.models.UserRequest;
import az.code.telegram_bot_api.models.enums.RequestStatus;
import az.code.telegram_bot_api.repositories.RequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class RequestSchedule {
    final
    RequestRepository requestRepository;
    final
    RabbitTemplate template;


    public RequestSchedule(RequestRepository requestRepository, RabbitTemplate template) {
        this.requestRepository = requestRepository;
        this.template = template;
    }

    @Scheduled(cron = "${cron.expiredTimeExpression}", zone = "Asia/Baku")
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
                template.convertAndSend(
                        RabbitMQConfig.exchange,
                        RabbitMQConfig.expired,
                        r.getUUID());
                r.setActive(false);
                requestRepository.save(r);
            }
        }
    }
}
