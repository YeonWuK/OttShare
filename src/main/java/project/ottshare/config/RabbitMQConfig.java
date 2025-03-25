package project.ottshare.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.queue.name}")
    private String queueName;

    @Value("${spring.rabbitmq.exchange.name}")
    private String exchangeName;

    // Queue 생성
    @Bean
    public Queue queue() {
        return new Queue(queueName, true);
    }

    /**
     * 지정된 Exchange 이름으로 Direct Exchange Bean 을 생성
     */
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("chat.room.#");
    }

    /**
     * RabbitMQ 연동을 위한 ConnectionFactory 빈을 생성하여 반환
     **/
    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setHost(rabbitMqProperties.getHost());
//        connectionFactory.setPort(rabbitMqProperties.getPort());
//        connectionFactory.setUsername(rabbitMqProperties.getUsername());
//        connectionFactory.setPassword(rabbitMqProperties.getPassword());
        return connectionFactory;
    }

    /**
     * RabbitTemplate
     * ConnectionFactory 로 연결 후 실제 작업을 위한 Template
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * 직렬화 (메세지를 JSON 으로 변환하는 Message Converter)
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
