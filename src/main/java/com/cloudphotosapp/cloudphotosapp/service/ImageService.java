package com.cloudphotosapp.cloudphotosapp.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface ImageService {

    @Transactional
    void deletePhotosById(Set<Long> photosId);

    @Transactional
    void saveImageFile(Long folderId, MultipartFile file);

}
