package com.laserdiffraction01.laserdiffraction01.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    @Transactional
    void saveImageFile(Long folderId, MultipartFile file);
}
