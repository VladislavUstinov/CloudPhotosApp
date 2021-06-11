package com.laserdiffraction01.laserdiffraction01.repository;

import com.laserdiffraction01.laserdiffraction01.domain.FilePhoto;
import org.springframework.data.repository.CrudRepository;

public interface FilePhotoRepository extends CrudRepository<FilePhoto, Long> {
    FilePhoto findByName(String name);
}
