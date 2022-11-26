package io.c0dr.filemanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class FileUploadResponse {
    private boolean isOk;
    private String fileName;
    private Integer documentId;
}
