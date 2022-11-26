package io.c0dr.filemanager.repository;


import io.c0dr.filemanager.model.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FileModelRepository extends JpaRepository<FileModel, Integer>, JpaSpecificationExecutor<FileModel> {
    FileModel findByPrivateUrlSuffix(String url);
}
