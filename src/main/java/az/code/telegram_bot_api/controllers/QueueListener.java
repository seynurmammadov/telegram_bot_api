package az.code.telegram_bot_api.controllers;

import az.code.telegram_bot_api.configs.RabbitMQConfig;
import az.code.telegram_bot_api.models.DTOs.RequestDTO;
import az.code.telegram_bot_api.models.DTOs.UserData;
import az.code.telegram_bot_api.models.Request;
import az.code.telegram_bot_api.services.interfaces.ListenerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QueueListener {
    final
    ListenerService listenerService;

    public QueueListener(ListenerService listenerService) {
        this.listenerService = listenerService;
    }

    @RabbitListener(queues = RabbitMQConfig.sent)
    public void saveRequest(UserData userData) {
        log.info("New request in 'sent' queue. UUID: {}", userData.getUUID());
        listenerService.saveRequest(userData);
    }
}
