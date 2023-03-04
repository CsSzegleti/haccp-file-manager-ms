package io.c0dr.filemanager.service.exception;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class ApiError {

    private String path;

    private String message;

    private Set<String> errors = new LinkedHashSet<>() ;


    @Builder
    public ApiError(String path, String message) {
        this.path = path;
        this.message = message;
    }


}