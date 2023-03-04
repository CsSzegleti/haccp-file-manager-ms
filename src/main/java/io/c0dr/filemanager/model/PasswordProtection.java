package io.c0dr.filemanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordProtection implements Serializable {
    private Integer documentId;
    private String userPassword;
    private String ownerPassword;
}

