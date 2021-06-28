package com.cloudphotosapp.cloudphotosapp.service;

import com.cloudphotosapp.cloudphotosapp.domain.FilePhoto;
import com.cloudphotosapp.cloudphotosapp.domain.Folder;
import com.cloudphotosapp.cloudphotosapp.repository.FilePhotoRepository;
import com.cloudphotosapp.cloudphotosapp.repository.FolderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService{


    private final FilePhotoRepository filePhotoRepository;
    private final FolderRepository folderRepository;

    public ImageServiceImpl(FilePhotoRepository filePhotoRepository, FolderRepository folderRepository) {
        this.filePhotoRepository = filePhotoRepository;
        this.folderRepository = folderRepository;
    }

    @Transactional
    @Override
    public void deletePhotosById(Set<Long> photosId) {
        if (photosId == null || photosId.isEmpty())
            return;

        //todo: точно не лучше просто вызвать filePhotoRepository.deleteAll и все?
        // todo: может нужно сохранить изменения в папке?

        Iterable<FilePhoto> iterablePhotos = filePhotoRepository.findAllById(photosId);
        if (iterablePhotos == null)
            return;

        Iterator<FilePhoto> iterPhotos = iterablePhotos.iterator();

        if (iterPhotos == null || iterPhotos.hasNext()==false)
            return;

        while (iterPhotos.hasNext()) {
            FilePhoto photo = iterPhotos.next();
            if (photo.getFolder() != null) {
                photo.getFolder().getFilePhotos().remove(photo);
                photo.setFolder(null);
            }
        }

        filePhotoRepository.deleteAll(iterablePhotos);
    }

    @Override
    @Transactional
    public void saveImageFile(Long folderId, MultipartFile file) {

        log.debug("ImageServiceImpl.saveImageFile()");
        try {
            Folder folder = folderRepository.findById(folderId).get();

            FilePhoto filePhoto = new FilePhoto();

            filePhoto.setName (file.getOriginalFilename());

            log.debug("file.getContentType() = " + file.getContentType());
            filePhoto.setContentType(file.getContentType());

            Byte[] byteObjects = new Byte[file.getBytes().length];

            int i = 0;

            for (byte b : file.getBytes()){
                byteObjects[i++] = b;
            }

            filePhoto.setImage(byteObjects);

            folder.addFilePhoto(filePhoto);

            folderRepository.save(folder);
            //filePhotoRepository.save(filePhoto); - why it's not needed???
        } catch (IOException e) {
            //todo handle better
            log.error("Error occurred", e);

            e.printStackTrace();
        }
    }

}
