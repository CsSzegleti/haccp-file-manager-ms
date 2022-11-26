package io.c0dr.filemanager.controller.amqp;

import io.c0dr.filemanager.controller.amqp.config.AMQPHeaderNames;
import io.c0dr.filemanager.controller.amqp.config.RequestQueueNamesConfig;
import io.c0dr.filemanager.model.FileModel;
import io.c0dr.filemanager.model.FileUploadResponse;
import io.c0dr.filemanager.service.FileRegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

import static io.c0dr.filemanager.controller.amqp.config.QueueModules.DOCUMENT_REGISTRATION_MODULE;


@Component
@RequiredArgsConstructor
@Slf4j
@Validated
public class DocumentRegistrationController {

    private final RequestQueueNamesConfig requestQueueNamesConfig;

    private final FileRegistrationService registrationService;

    private final MessageValidator validator;

    @RabbitListener(queues = "#{@requestQueueNamesConfig.getRequestQueueNames('document-registration')}",
            errorHandler = "AMQPErrorHandler")
    @SendTo("!{result.getHeaders().get('responseQueue')}")
    public Message<FileUploadResponse> registerDocument(@Valid FileModel fileModel,
                                                        @Headers Map<String, Object> headers,
                                                        @org.springframework.messaging.handler.annotation.Header(AmqpHeaders.CONSUMER_QUEUE) String consumerQueue) throws IOException {

        validator.validateHeader(headers);

        var uploadResponse = registrationService.processFileRegistration(fileModel);

        return MessageBuilder.withPayload(uploadResponse)
                .setHeader(
                        AMQPHeaderNames.RESPONSE_QUEUE_NAME,
                        requestQueueNamesConfig.getResponseQueueName(DOCUMENT_REGISTRATION_MODULE, consumerQueue))
                .setHeader(
                        AMQPHeaderNames.MESSAGE_CORRELATION_ID,
                        headers.get(AMQPHeaderNames.MESSAGE_CORRELATION_ID))
                .build();
    }
}
