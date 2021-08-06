package az.code.telegram_bot_api.controllers;

import az.code.telegram_bot_api.configs.RabbitMQConfig;
import az.code.telegram_bot_api.models.AcceptedOffer;
import az.code.telegram_bot_api.models.DTOs.UserData;
import az.code.telegram_bot_api.services.interfaces.ListenerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QueueListener {

    ListenerService listenerService;

    public QueueListener(ListenerService listenerService) {
        this.listenerService = listenerService;
    }

    @RabbitListener(queues = RabbitMQConfig.sent)
    public void saveRequest(UserData userData) throws JsonProcessingException {
        log.info("New request in 'sent' queue. UUID: {}", userData.getUUID());
        listenerService.saveRequest(userData);
    }

    @RabbitListener(queues = RabbitMQConfig.cancelled)
    public void cancelRequest(String UUID) {
        log.info("New request in 'cancelled' queue. UUID: {}", UUID);
        listenerService.cancelRequest(UUID);
    }

    @RabbitListener(queues = RabbitMQConfig.accepted)
    public void acceptOffer(AcceptedOffer offer) {
        log.info("New request in 'accepted' queue. For user: {}", offer.getAgentUsername());
        listenerService.acceptedOffer(offer);
    }

}
