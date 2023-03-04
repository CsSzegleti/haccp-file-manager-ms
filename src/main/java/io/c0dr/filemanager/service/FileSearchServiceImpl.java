package io.c0dr.filemanager.service;

import io.c0dr.filemanager.controller.converter.SearchResultConverter;
import io.c0dr.filemanager.model.SearchResult;
import io.c0dr.filemanager.repository.FileModelRepository;
import io.c0dr.filemanager.service.exception.ErrorCode;
import io.c0dr.filemanager.service.exception.MissingEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FileSearchServiceImpl implements FileSearchService {

    private final FileModelRepository fileRepository;
    private final SearchResultConverter searchResultConverter;
    private final FileUrlService fileUrlService;
    @Value("${file.url.lifetime}")
    private Long DEFAULT_URL_LIFE_TIME;


    @Override
    public SearchResult getFileByIdToPublic(Integer fileId, Long urlLifeTimeInMinutes) throws MissingEntityException {
        var resultFile = fileRepository.findById(fileId);

        if (resultFile.isPresent()) {
            String urlSuffix = fileUrlService.generateUrlSuffix();
            fileUrlService.registerPublicUrlForDocument(resultFile.get(), urlSuffix,
                    urlLifeTimeInMinutes == null || urlLifeTimeInMinutes == 0L ?
                            DEFAULT_URL_LIFE_TIME : urlLifeTimeInMinutes);
            return searchResultConverter.toExternal(resultFile.get(), urlSuffix);
        } else {
            throw new MissingEntityException(ErrorCode.ErrorType.MISSING_ENTITY, "Cannot find file with id: " + fileId);
        }
    }

    @Override
    public SearchResult getFileByIdToPrivate(Integer fileId) throws MissingEntityException {
        var resultFile = fileRepository.findById(fileId);

        if (resultFile.isPresent()) {
            return searchResultConverter.toExternal(resultFile.get(), resultFile.get().getPrivateUrlSuffix());
        }
        else {
            throw new MissingEntityException(ErrorCode.ErrorType.MISSING_ENTITY, "Cannot find file with id: " + fileId);
        }
    }
}
