package io.c0dr.filemanager.model;

import io.c0dr.filemanager.controller.rest.config.RequestMetaDataBean;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileModelBD extends RequestMetaDataBean {
    private byte[] data;
    private String originalFileName;
    private ExtraData extraData;
    private String uploadIp;
    private String uploader;
}
