package io.c0dr.filemanager.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UrlFileModel {
    private Integer id;
    private String originalFileName;
    private String relativePath;
}
