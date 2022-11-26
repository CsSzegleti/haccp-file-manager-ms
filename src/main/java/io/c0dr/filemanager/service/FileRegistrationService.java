package io.c0dr.filemanager.service;

import io.c0dr.filemanager.model.FileModelBD;
import io.c0dr.filemanager.model.FileUploadResponse;

import java.io.IOException;

public interface FileRegistrationService {
    FileUploadResponse processFileRegistration(FileModelBD data) throws IOException;
}
