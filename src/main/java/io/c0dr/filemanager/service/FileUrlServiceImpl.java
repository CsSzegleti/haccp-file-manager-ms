package io.c0dr.filemanager.service;

import io.c0dr.filemanager.client.redis.RedisClient;
import io.c0dr.filemanager.model.FileModel;
import io.c0dr.filemanager.repository.FileModelRepository;
import io.c0dr.filemanager.service.model.UrlFileModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUrlServiceImpl implements FileUrlService {

    private final RedisClient redisClient;
    private final FileModelRepository documentRepository;
    @Value("${file.url.base}")
    private URL fileUrlContext;

    @Override
    public String generateUrlSuffix() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void registerPublicUrlForDocument(FileModel document, String urlSuffix, long minutesToLive) {
        redisClient.addUrl(urlSuffix,
                UrlFileModel.builder().id(document.getId())
                        .originalFileName(document.getFileName())
                        .relativePath(document.getRelativePath())
                        .build(), minutesToLive);
    }

    @Override
    public UrlFileModel getLocation(String urlSuffix, String user) {

        if (urlSuffix == null) {
            return null;
        }

        UrlFileModel publicUrlFileModel = getPublicLocation(urlSuffix);

        if (null != publicUrlFileModel) {
            return publicUrlFileModel;
        }

        UrlFileModel privateUrlFileModel = getPrivateLocation(urlSuffix);
        return privateUrlFileModel;

    }

    @Override
    public String createFullUrl(String urlSuffix) {
        if (null == urlSuffix) {
            return null;
        }

        try {
            return new URL(fileUrlContext, urlSuffix).toString();
        } catch (MalformedURLException e) {
            log.error(e.getLocalizedMessage(), e);
        }

        return null;
    }

    private UrlFileModel getPublicLocation(String urlSuffix) {
        UrlFileModel urlFileModel = redisClient.getLocationFromUrlSuffix(urlSuffix);

        if (urlFileModel != null) {
            log.info("File location for {} queried via public url", urlFileModel);
        }

        return urlFileModel;
    }

    private UrlFileModel getPrivateLocation(String urlSuffix) {

        var file = documentRepository.findByPrivateUrlSuffix(urlSuffix);

        if (null == file) {
            return null;
        }

        return UrlFileModel.builder()
                .id(file.getId())
                .originalFileName(file.getFileName())
                .relativePath(file.getRelativePath())
                .build();
    }
}
