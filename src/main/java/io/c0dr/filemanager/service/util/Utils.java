package io.c0dr.filemanager.service.util;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class Utils {

    public String getMimeType(File file) throws IOException {
        Tika tika = new Tika();
        return tika.detect(file);
    }
}
