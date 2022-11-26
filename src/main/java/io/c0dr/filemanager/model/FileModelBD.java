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
import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "file")
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@TypeDefs({
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class FileModelBD implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "file_name", length = 200)
    private String fileName;

    @Column(name = "file_extension", length = 50)
    private String fileExtension;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "version", length = 50)
    private String version;

    @Column(name = "downloaded_at")
    private Instant downloadedAt;

    @Column(name = "downloaded_by", length = 50)
    private String downloadedBy;

    @Column(name = "hash", length = 64)
    private String fileHash;

    @Column(name = "uploaded_at")
    private Instant uploadedAt;

    @Column(name = "uploader", length = 100)
    private String uploader;

    @Column(name = "uploader_ip", length = 39)
    private String uploadIp;

    @Column(name = "virus_checked")
    private Boolean isVirusCheck;

    @Column(name = "url_suffix", length = 500)
    private String privateUrlSuffix;

    @Column(name = "relative_path", length = 255)
    private String relativePath;

    @Type(type = "jsonb")
    @Column(name = "meta_data")
    private Map<String, String> metaData;
}
