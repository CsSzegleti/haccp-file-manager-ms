package io.c0dr.filemanager.controller.amqp.error;

import io.c0dr.filemanager.controller.amqp.config.RequestQueueNamesConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

import static io.c0dr.filemanager.controller.amqp.config.AMQPHeaderNames.MESSAGE_CORRELATION_ID;

@RequiredArgsConstructor
@Component(value = "AMQPErrorHandler")
@Slf4j
public class AMQPErrorHandler implements RabbitListenerErrorHandler {

    private final RequestQueueNamesConfig requestQueueNamesConfig;

    private final RabbitTemplate rabbitTemplate;

    @Override
    public Object handleError(Message message, org.springframework.messaging.Message<?> message1, ListenerExecutionFailedException e) {

        log.error("There has been an exception during resolving the message", e);

        rabbitTemplate.convertAndSend(requestQueueNamesConfig
                .getResponseQueueNameFromAllModules(
                        message.getMessageProperties().getConsumerQueue()),
                e.getMessage(),
                message2 -> {
                    message2.getMessageProperties().setHeader(MESSAGE_CORRELATION_ID, message.getMessageProperties().getHeader(MESSAGE_CORRELATION_ID));
                    return message2;
                }
        );

        return null;
    }
}
