package io.c0dr.filemanager.service;

import org.springframework.core.io.Resource;

public interface FileReaderService {
    Resource loadFileAsResource(String fileName);
}
