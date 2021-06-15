package com.laserdiffraction01.laserdiffraction01.service;

import com.laserdiffraction01.laserdiffraction01.domain.FilePhoto;
import com.laserdiffraction01.laserdiffraction01.domain.Folder;
import com.laserdiffraction01.laserdiffraction01.repository.FilePhotoRepository;
import com.laserdiffraction01.laserdiffraction01.repository.FolderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CopyPasteServiceImpl implements CopyPasteService {
    private final FolderRepository folderRepository;
    private final FilePhotoRepository filePhotoRepository;


    List<Long> copiedPhotosIds = new ArrayList<>();
    List<Long> copiedFoldersIds = new ArrayList<>();

    public CopyPasteServiceImpl(FolderRepository folderRepository, FilePhotoRepository filePhotoRepository) {
        this.folderRepository = folderRepository;
        this.filePhotoRepository = filePhotoRepository;
    }

    @Override
    public void setCopied(List<Long> copiedPhotosIds, List<Long> copiedFoldersIds) {
        this.copiedFoldersIds = copiedFoldersIds;
        this.copiedPhotosIds = copiedPhotosIds;
    }

    public boolean pasteFilePhoto(Folder currentFolder, Long filePhotoId){
        Optional<FilePhoto> answer = filePhotoRepository.findById(filePhotoId);

        if (!answer.isPresent()){
            log.error ("CopyPasteService.pasteFilePhoto: PHOTO NOT FOUND, photo id = " + filePhotoId);
            return false;
        }

        FilePhoto filePhotoOld = answer.get();

        //выполняю глубокую копию только если копирование идёт в папку, отличную от той, где фото лежала раньше
        if (!filePhotoOld.getFolder().getId().equals(currentFolder.getId())) {
            FilePhoto filePhotoNew = FilePhoto.deepCopyWithoutId(filePhotoOld);
            currentFolder.addFilePhoto(filePhotoNew);
        }

        return true;
    }

    public void pasteFolder (Folder currentFolder, Folder oldFolder) {
        Folder newFolder = new Folder();

        newFolder.setName(oldFolder.getName());

        currentFolder.addSubFolder(newFolder);//прописали parent-subfolder

        //создаю новые фото глубокой копией
        if (oldFolder.getFilePhotos() != null)
            for (FilePhoto filePhotoOld : oldFolder.getFilePhotos()) {
                FilePhoto filePhotoNew = FilePhoto.deepCopyWithoutId(filePhotoOld);
                newFolder.addFilePhoto(filePhotoNew);
            }

        //передаю права на подпапку владельцам currentFolder
        newFolder.getOwners().addAll(currentFolder.getOwners());

        //создаю новые подпапки глубокой копией
        if (oldFolder.getSubFolders() != null)
            for (Folder subFolder : oldFolder.getSubFolders())
                pasteFolder (newFolder, subFolder);
    }

    public boolean pasteFolder(Folder currentFolder, Long copiedFolderId) {
        Optional<Folder> answer = folderRepository.findById(copiedFolderId);

        if (!answer.isPresent()) {
            log.error("CopyPasteService.pasteFolder: FOLDER NOT FOUND, copiedFolderId = " + copiedFolderId);
            return false;
        }

        Folder oldFolder = answer.get();

        pasteFolder(currentFolder, oldFolder);

        return true;
    }



    @Override
    public boolean pasteCopied(Long currentFolderId) {
        //get current folder by id:
        Optional<Folder> answer = folderRepository.findById(currentFolderId);

        if ( !answer.isPresent()){
            log.error("CopyPasteService.pasteCopied(...).findById(currentFolderId) returned null. folderId = " + currentFolderId);
            return false;
        }

        Folder currentFolder = answer.get();

        if (copiedFoldersIds != null)
            for (Long copiedFolderId : copiedFoldersIds)
                if (!copiedFolderId.equals(currentFolderId))
                    pasteFolder(currentFolder, copiedFolderId);

        if (copiedPhotosIds != null)
            for (Long copiedPhotoId : copiedPhotosIds)
                pasteFilePhoto(currentFolder, copiedPhotoId);

        folderRepository.save(currentFolder);

        return true;

    }
}
