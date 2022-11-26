package io.c0dr.filemanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class SearchResult implements Serializable {
    private Integer fileId;
    private String fileName;
    private String fileExtension;
    private Long fileSize;
    private String uploader;
    private Instant uploadedAt;
    private String url;
}
