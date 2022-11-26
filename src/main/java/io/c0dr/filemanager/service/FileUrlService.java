package io.c0dr.filemanager.service;


import io.c0dr.filemanager.model.FileModelBD;
import io.c0dr.filemanager.service.model.UrlFileModel;

public interface FileUrlService {

    String generateUrlSuffix();

    void registerPublicUrlForDocument(FileModelBD document, String urlSuffix, long minutesToLive);

    UrlFileModel getLocation(String urlSuffix, String user);

    String createFullUrl(String urlSuffix);

    void setDownloader(Integer docId, String user);
}
