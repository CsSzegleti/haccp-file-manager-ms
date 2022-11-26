package io.c0dr.filemanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileReaderServiceImpl implements FileReaderService {

    @Value("${file.folder-uri}")
    private String folderUrl;

    private Path fileStorageLocation;

    @PostConstruct
    public void init() throws Exception {
        this.fileStorageLocation = Paths.get(folderUrl)
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            log.info("loadFileAsResource: " + fileName);
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            log.info("loadFileAsResource: filepath:  " + filePath.toUri());
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException(resource.getDescription());
            }
        } catch (MalformedURLException | FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static InputStream getResourceFileAsInputStream(String fileName) {
        ClassLoader classLoader = FileReaderServiceImpl.class.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }


}
