package com.laserdiffraction01.laserdiffraction01.service;

import com.laserdiffraction01.laserdiffraction01.domain.FilePhoto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface ImageService {

    @Transactional
    void deletePhotosById(Set<Long> photosId);

    @Transactional
    void saveImageFile(Long folderId, MultipartFile file);

}
