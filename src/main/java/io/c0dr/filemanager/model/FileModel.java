package io.c0dr.filemanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class FileModel implements Serializable {
    @Size(max = 200, message = "error.fileName.length")
    @NotBlank(message = "error.fileName.empty")
    String fileName;

    @Size(max = 50, message = "error.extension.length")
    @NotBlank(message = "error.'${validatedValue}'.empty")
    String fileExtension;

    @Min(value = 0, message = "error.'${validatedValue}'.invalid")
    @NotNull(message = "error.'${validatedValue}'.empty")
    Long fileSize;

    @Size(max = 100, message = "error.'${validatedValue}'.length")
    @NotBlank(message = "error.'${validatedValue}'.empty")
    String documentHash;

    Instant uploadedAt;

    @Size(max = 100, message = "error.'${validatedValue}'.length")
    String uploader;

    @Size(max = 39, message = "error.'${validatedValue}'.length")
    String uploadIp;

    Boolean isVirusCheck;

    Instant examinedAt;

    Integer examinedBy;

    Boolean isApproved;

    @Size(max = 100, message = "error.'${validatedValue}'.length")
    String rejectReason;

    @Pattern(regexp = "^((?!\\.\\.).)*$", message = "error.'${validatedValue}'.path.invalid")
    @Size(max = 255, message = "error.'${validatedValue}'.length")
    @NotBlank(message = "error.'${validatedValue}'.empty")
    String relativePath;

    Map<String, String> metaData;

}
