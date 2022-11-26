package io.c0dr.filemanager.service;

import io.c0dr.filemanager.model.FileModelBD;
import io.c0dr.filemanager.model.FileUploadResponse;
import io.c0dr.filemanager.repository.FileModelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileRegistrationServiceImpl implements FileRegistrationService {

    private final FileModelRepository fileRepository;

    private final FileUrlService fileUrlService;

    public FileUploadResponse processFileRegistration(FileModelBD fileModel) {
        FileUploadResponse uploadResponse = new FileUploadResponse();

        if (fileModel != null) {
            try {
                uploadResponse.setFileName(fileModel.getRelativePath());
                uploadResponse.setOk(true);
                fileModel.setPrivateUrlSuffix(fileUrlService.generateUrlSuffix());

                fileRepository.save(fileModel);
                uploadResponse.setDocumentId(fileModel.getId());
            } catch (Exception ex) {
                uploadResponse.setOk(false);
                log.error("Document registration failed - {}", fileModel);
            }

        }

        log.info("Document registration successful - {}", fileModel);

        return uploadResponse;
    }
}
