package com.laserdiffraction01.laserdiffraction01.service;

import com.laserdiffraction01.laserdiffraction01.domain.FilePhoto;
import com.laserdiffraction01.laserdiffraction01.domain.Folder;
import com.laserdiffraction01.laserdiffraction01.repository.FilePhotoRepository;
import com.laserdiffraction01.laserdiffraction01.repository.FolderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

        try {
            Folder folder = folderRepository.findById(folderId).get();

            FilePhoto filePhoto = new FilePhoto();

            filePhoto.setName (file.getOriginalFilename());

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
