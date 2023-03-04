package io.c0dr.filemanager.controller.rest.config;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class RequestMetaDataBean {
    protected String userName;
    protected String correlationId;
    protected String responseQueueName;
}
