package com.co.ke.paymentservice.moneypal.configs;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${queueName}")
    String queueName;

    @Value("${queueName1}")
    String queueName1;

    @Value("${exchange}")
    String exchange;

    @Value("${bindingKey}")
    private String routingkey;


    @Value("${bindingKey1}")
    private String routingkey1;

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }
//==========================================================
    @Bean
    Queue queue1() {
        return new Queue(queueName1, false);
    }
    @Bean
    Binding binding1(Queue queue1, DirectExchange exchange) {
        return BindingBuilder.bind(queue1).to(exchange).with(routingkey);
    }
    //===================================================
    @Bean
    DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingkey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    @Qualifier("rabbitTemplate1")
    public AmqpTemplate rabbitTemplate1(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate1 = new RabbitTemplate(connectionFactory);
        rabbitTemplate1.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate1;
    }
}
