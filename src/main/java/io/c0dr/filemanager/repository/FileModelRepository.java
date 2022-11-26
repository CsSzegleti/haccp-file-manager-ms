package io.c0dr.filemanager.repository;


import io.c0dr.filemanager.model.FileModelBD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FileModelRepository extends JpaRepository<FileModelBD, Integer>, JpaSpecificationExecutor<FileModelBD> {
    FileModelBD findByPrivateUrlSuffix(String url);
}
