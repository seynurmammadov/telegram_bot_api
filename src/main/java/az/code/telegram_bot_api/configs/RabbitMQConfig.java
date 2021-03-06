package az.code.telegram_bot_api.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;


@Setter
@Getter
@Configuration
public class RabbitMQConfig {

    public final static String cancelled = "cancelled";
    public final static String offered = "offered";
    public final static String sent = "sent";
    public final static String accepted = "accepted";
    public final static String expired = "expired";
    public final static String exchange = "exchange";

    @Value("${CLOUDAMQP_URL}")
    String CLOUDAMQP_URL;

    @Bean
    public Queue queueCancelled() {
        return new Queue(cancelled, true);
    }

    @Bean
    public Queue queueOffered() {
        return new Queue(offered, true);
    }

    @Bean
    public Queue queueSent() {
        return new Queue(sent, true);
    }

    @Bean
    public Queue queueAccepted() {
        return new Queue(accepted, true);
    }

    @Bean
    public Queue queueExpired() {
        return new Queue(expired, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding bindingOffered(TopicExchange exchange) {
        return BindingBuilder.bind(queueOffered()).to(exchange).with(queueOffered().getName());
    }

    @Bean
    public Binding bindingCancelled(TopicExchange exchange) {
        return BindingBuilder.bind(queueCancelled()).to(exchange).with(queueCancelled().getName());
    }

    @Bean
    public Binding bindingSent(TopicExchange exchange) {
        return BindingBuilder.bind(queueSent()).to(exchange).with(queueSent().getName());
    }

    @Bean
    public Binding bindingAccepted(TopicExchange exchange) {
        return BindingBuilder.bind(queueAccepted()).to(exchange).with(queueAccepted().getName());
    }

    @Bean
    public Binding bindingExpired(TopicExchange exchange) {
        return BindingBuilder.bind(queueExpired()).to(exchange).with(queueExpired().getName());
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

    @Bean
    public ConnectionFactory connectionFactory() throws URISyntaxException {
        final URI rabbitMqUrl = new URI(CLOUDAMQP_URL);
        final CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setUri(rabbitMqUrl);
        return factory;
    }
}
