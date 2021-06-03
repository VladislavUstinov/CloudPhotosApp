package com.laserdiffraction01.laserdiffraction01.repository;

import com.laserdiffraction01.laserdiffraction01.domain.FilePhoto;
import com.laserdiffraction01.laserdiffraction01.domain.Role;
import org.springframework.data.repository.CrudRepository;

public interface FilePhotoRepository extends CrudRepository<FilePhoto, Long> {
    FilePhoto findByName(String name);
}
