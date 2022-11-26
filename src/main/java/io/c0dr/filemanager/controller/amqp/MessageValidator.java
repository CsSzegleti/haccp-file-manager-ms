package io.c0dr.filemanager.controller.amqp;

import io.c0dr.filemanager.controller.amqp.config.AMQPHeaderNames;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@Component
public class MessageValidator {
    public void validateHeader(Map<String, Object> headers) {
        validateHeaderCorrelationId(headers);
    }

    private void validateHeaderCorrelationId(Map<String, Object> headers) {
        if ((CollectionUtils.isEmpty(headers) || StringUtils.isBlank((String) headers.get(AMQPHeaderNames.MESSAGE_CORRELATION_ID)))) {
            throw new IllegalArgumentException("error.correlationId.empty");
        }
    }
}
