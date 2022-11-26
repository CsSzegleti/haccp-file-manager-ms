package io.c0dr.filemanager.service;


import io.c0dr.filemanager.model.FileModel;
import io.c0dr.filemanager.service.model.UrlFileModel;

public interface FileUrlService {

    String generateUrlSuffix();

    void registerPublicUrlForDocument(FileModel document, String urlSuffix, long minutesToLive);

    UrlFileModel getLocation(String urlSuffix, String user);

    String createFullUrl(String urlSuffix);
}
