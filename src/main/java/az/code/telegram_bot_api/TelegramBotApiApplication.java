package az.code.telegram_bot_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TelegramBotApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotApiApplication.class, args);
    }

}
