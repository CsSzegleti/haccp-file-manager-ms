package io.c0dr.filemanager.service;

import io.c0dr.filemanager.model.SearchResult;
import io.c0dr.filemanager.service.exception.MissingEntityException;

public interface FileSearchService {

    SearchResult getFileByIdToPublic(Integer fileId, Long urlLifeTimeInMinutes) throws MissingEntityException;

    SearchResult getFileByIdToPrivate(Integer fileId) throws MissingEntityException;
}
