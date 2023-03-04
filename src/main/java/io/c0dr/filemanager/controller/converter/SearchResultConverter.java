package io.c0dr.filemanager.controller.converter;

import io.c0dr.filemanager.model.FileModel;
import io.c0dr.filemanager.model.SearchResult;
import io.c0dr.filemanager.service.FileUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchResultConverter {

    private final FileUrlService urlService;

    public SearchResult toExternal(FileModel docModel, String urlSuffix) {
        return SearchResult.builder()
                .fileId(docModel.getId())
                .fileName(docModel.getFileName())
                .fileExtension(docModel.getFileExtension())
                .fileSize(docModel.getFileSize())
                .uploadedAt(docModel.getUploadedAt())
                .uploader(docModel.getUploader())
                .url(urlService.createFullUrl(urlSuffix))
                .build();
    }
}
