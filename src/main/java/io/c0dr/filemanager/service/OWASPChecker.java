package io.c0dr.filemanager.service;


import com.google.common.hash.Hashing;
import io.c0dr.filemanager.service.util.Utils;
import io.c0dr.filemanager.model.Allowed;
import io.c0dr.filemanager.model.FileModel;
import io.c0dr.filemanager.service.exception.ErrorCode;
import io.c0dr.filemanager.service.exception.SecurityConstraintException;
import io.c0dr.filemanager.service.exception.Severity;
import io.c0dr.filemanager.service.exception.WriteException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OWASPChecker {

    @Value("${file.folder-uri.tmp}")
    private String tmpFolderUri;

    @Value("${file.folder-uri.final}")
    private String finalFolderUri;

    @Value("${file.name.max-length}")
    private int fileNameMaxLength;

    private final Utils utils;

    private final String NOT_ALLOWED_CHARS = ".*[^a-zA-Z\\d\\-_].*";

    private final String ALLOWED_SPECIAL_CHARS = "[ ().]";

    public File saveToTmpLocation(byte[] data, String originalFileName) throws WriteException {
        File f = new File(tmpFolderUri, createUniqueFileName(originalFileName));

        try(FileOutputStream outputStream = new FileOutputStream(f)) {
            outputStream.write(data);
        } catch (IOException e) {
            log.error("The file could not be saved.");
            log.error(e.getMessage());
            throw new WriteException("error.file.can.not.be.saved", e);
        }
        return f;
    }

    public void copyToPermanentLocation(File sourceFile) throws WriteException {
        File destinationFile = new File(finalFolderUri, sourceFile.getName());
        try {
            FileUtils.copyFile(sourceFile, destinationFile);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new WriteException("error.file.can.not.be.saved", e);
        }
    }

    private String getExtension(String fileName) {
        return FilenameUtils.getExtension(fileName);
    }

    public String createUniqueFileName(String fileName) {
        return String.format("%s-%s.%s", UUID.randomUUID(), Instant.now().toEpochMilli(), getExtension(fileName));
    }

    public FileModel fillDocFileModel(Map<String, String> metadata, File file)
            throws IOException {
        return FileModel.builder()
                .fileName(file.getName())
                .fileSize(file.length())
                .fileHash(com.google.common.io.Files.asByteSource(file).hash(Hashing.sha256()).toString())
                .uploadedAt(Instant.now())
                .relativePath(file.getName())
                .metaData(metadata)
                .fileExtension(getExtension(file.getName()))
                .build();
    }

    public boolean deleteFile (File file) {
        boolean result =  file.delete();
        if(!result) log.warn("The file with name " + file.getName() + " at path " + file.getAbsolutePath() + " could not be deleted.");
        return result;
    }

    public void checkFile(File fileToCheck) throws SecurityConstraintException, IOException {
        validateExtension(fileToCheck);
        validateContentType(fileToCheck);
    }

    public void validateExtension(File fileToCheck) throws SecurityConstraintException {
        if (fileToCheck != null) {
            String extension = FilenameUtils.getExtension(fileToCheck.getName());
            log.info("Extension: " + extension);
            if (!Allowed.extensions.contains(extension.toLowerCase())) {
                log.warn("The file extension is not allowed.");
                throw new SecurityConstraintException(new ErrorCode(ErrorCode.ErrorType.INVALID_ENTITY, Severity.critical),
                        "error.file.security.extension.not.allowed");
            }
        }
    }

    public void validateContentType(File fileToCheck) throws IOException, SecurityConstraintException {
        String mimeType = utils.getMimeType(fileToCheck);
        log.info("Mimetype: " + mimeType);
        if (!Allowed.mimeTypes.contains(mimeType.toLowerCase())) {
            log.warn("The file mimetype is not allowed.");
            throw new SecurityConstraintException(new ErrorCode(ErrorCode.ErrorType.INVALID_ENTITY, Severity.critical),
                    "error.file.security.mimetype.not.allowed");
        }
    }

    public String validateFileName(String fileName) throws SecurityConstraintException {

        int lastDot = fileName.lastIndexOf('.');

        if (lastDot == -1) {
            throw new SecurityConstraintException(new ErrorCode(ErrorCode.ErrorType.INVALID_ENTITY, Severity.critical),
                    "error.file.security.extension.not.allowed");
        }

        String fileNameWithoutExtension = fileName.substring(0, lastDot);
        String extension = fileName.substring(lastDot);

        fileNameWithoutExtension = replaceSpecialChars(fileNameWithoutExtension);

        // check not allowed characters
        if(fileNameWithoutExtension.matches(NOT_ALLOWED_CHARS)) {
            log.warn("Filename contains not valid character(s).");
            throw new SecurityConstraintException(new ErrorCode(ErrorCode.ErrorType.INVALID_ENTITY, Severity.critical),
                    "error.file.security.name.not.allowed.character");
        }

        // check length
        if(fileNameWithoutExtension.length() > fileNameMaxLength || fileNameWithoutExtension.length() == 0)   {
            log.warn("Filename length is not acceptable.");
            throw new SecurityConstraintException(new ErrorCode(ErrorCode.ErrorType.INVALID_ENTITY, Severity.critical),
                    "error.file.security.name.not.acceptable");
        }
        log.info("Filename \"" + fileNameWithoutExtension + extension + "\" is valid.");
        return fileNameWithoutExtension + extension;
    }

    private String replaceSpecialChars(String fileNameWithoutExtension) {
        fileNameWithoutExtension = fileNameWithoutExtension.replaceAll("é", "e");
        fileNameWithoutExtension = fileNameWithoutExtension.replaceAll("á", "a");
        fileNameWithoutExtension = fileNameWithoutExtension.replaceAll("ű", "u");
        fileNameWithoutExtension = fileNameWithoutExtension.replaceAll("ú", "u");
        fileNameWithoutExtension = fileNameWithoutExtension.replaceAll("ő", "o");
        fileNameWithoutExtension = fileNameWithoutExtension.replaceAll("ú", "u");
        fileNameWithoutExtension = fileNameWithoutExtension.replaceAll("ö", "o");
        fileNameWithoutExtension = fileNameWithoutExtension.replaceAll("ó", "o");
        fileNameWithoutExtension = fileNameWithoutExtension.replaceAll("í", "i");

        fileNameWithoutExtension = fileNameWithoutExtension.replaceAll("É", "E");
        fileNameWithoutExtension = fileNameWithoutExtension.replaceAll("Á", "A");
        fileNameWithoutExtension = fileNameWithoutExtension.replaceAll("Ű", "U");
        fileNameWithoutExtension = fileNameWithoutExtension.replaceAll("Ú", "U");
        fileNameWithoutExtension = fileNameWithoutExtension.replaceAll("Ő", "O");
        fileNameWithoutExtension = fileNameWithoutExtension.replaceAll("Ú", "U");
        fileNameWithoutExtension = fileNameWithoutExtension.replaceAll("Ö", "O");
        fileNameWithoutExtension = fileNameWithoutExtension.replaceAll("Ó", "O");
        fileNameWithoutExtension = fileNameWithoutExtension.replaceAll("Í", "I");

        return fileNameWithoutExtension;
    }
}
