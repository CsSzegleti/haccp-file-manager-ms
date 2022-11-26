package io.c0dr.filemanager.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "files")
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@TypeDefs({
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class FileModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "file_name", length = 200)
    @Size(max = 200, message = "error.fileName.length")
    @NotBlank(message = "error.fileName.empty")
    private String fileName;

    @Column(name = "file_extension", length = 50)
    @Size(max = 50, message = "error.extension.length")
    @NotBlank(message = "error.extension.empty")
    private String fileExtension;

    @Column(name = "file_size")
    @Min(value = 0, message = "error.size.invalid")
    @NotNull(message = "error.size.empty")
    private Long fileSize;

    @Column(name = "hash", length = 64)
    @Size(max = 64, message = "error.hash.length")
    @NotBlank(message = "error.hash.empty")
    private String fileHash;

    @Column(name = "uploaded_at")
    private Instant uploadedAt;

    @Column(name = "uploader", length = 100)
    @Size(max = 100, message = "error.uploader.length")
    private String uploader;

    @Column(name = "uploader_ip", length = 39)
    @Size(max = 39, message = "error.ip.length")
    private String uploadIp;

    @Column(name = "virus_checked")
    private Boolean isVirusCheck;

    @Column(name = "url_suffix", length = 500)
    private String privateUrlSuffix;

    @Column(name = "relative_path", length = 255)
    @Pattern(regexp = "^((?!\\.\\.).)*$", message = "error.path.path.invalid")
    @Size(max = 255, message = "error.path.length")
    @NotBlank(message = "error.path.empty")
    private String relativePath;

    @Type(type = "jsonb")
    @Column(name = "meta_data")
    private Map<String, String> metaData;
}
