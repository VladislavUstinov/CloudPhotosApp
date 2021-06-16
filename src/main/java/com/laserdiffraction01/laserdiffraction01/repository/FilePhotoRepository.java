package com.laserdiffraction01.laserdiffraction01.repository;

import com.laserdiffraction01.laserdiffraction01.domain.FilePhoto;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FilePhotoRepository extends CrudRepository<FilePhoto, Long> {
    FilePhoto findByName(String name);

    List<FilePhoto> findByNameContaining (String name);
}
