package io.c0dr.filemanager.controller.rest.converter;

import io.c0dr.filemanager.controller.rest.config.RequestMetaDataBean;
import io.c0dr.filemanager.model.ExtraData;
import io.c0dr.filemanager.model.FileModelBD;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Component
@Slf4j
public class RestMessageConverter {


    @Value("${amqp.file-upload.queue.response}")
    protected String responseQueueName;

    @Value("${amqp.file-upload.queue.request}")
    protected String requestQueueName;
    @Autowired
    RequestMetaDataBean communicationMetaDataBean;

    public FileModelBD convert(byte[] data, String originalFileName,
                               ExtraData extraData, HttpServletRequest request) {
        FileModelBD fileModelBD = FileModelBD.builder()
                .data(data)
                .originalFileName(originalFileName)
                .extraData(extraData)
                .uploadIp(request.getRemoteAddr())
                .uploader(request.getUserPrincipal() == null ? null : request.getUserPrincipal().getName())
                .build();
        fileModelBD.setUserName(communicationMetaDataBean.getUserName());
        fileModelBD.setCorrelationId(communicationMetaDataBean.getCorrelationId());
        fileModelBD.setResponseQueueName(responseQueueName);
        return fileModelBD;
    }
}
