package com.laserdiffraction01.laserdiffraction01.service;

import com.laserdiffraction01.laserdiffraction01.DTO.FoldersPhotosDTO;
import com.laserdiffraction01.laserdiffraction01.domain.FilePhoto;
import com.laserdiffraction01.laserdiffraction01.domain.Folder;
import com.laserdiffraction01.laserdiffraction01.domain.User;
import com.laserdiffraction01.laserdiffraction01.repository.FilePhotoRepository;
import com.laserdiffraction01.laserdiffraction01.repository.FolderRepository;
import com.laserdiffraction01.laserdiffraction01.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class FolderServiceImpl implements FolderService {
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final FilePhotoRepository filePhotoRepository;


    public FolderServiceImpl(UserRepository userRepository, FolderRepository folderRepository, FilePhotoRepository filePhotoRepository) {
        this.userRepository = userRepository;
        this.folderRepository = folderRepository;
        this.filePhotoRepository = filePhotoRepository;
    }

    @Override
    public Folder getRootFolderByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null)
            throw new UsernameNotFoundException("User {" + username + "} not found");

        return user.getRoot();
    }

    @Override
    public Folder getFolderById(Long id) {
        Optional<Folder> answer = folderRepository.findById(id);

        if ( answer.isPresent())
            return answer.get();
        else
            return null;
    }

    @Transactional
    @Override
    public void deleteFoldersById(Set<Long> foldersIds) {
        log.debug ("FolderServiceImpl.deleteFolders(..)");


        for (Long folderId : foldersIds) {
            Optional<Folder> folderOpt = folderRepository.findById(folderId);
            if (folderOpt.isPresent()==false)
                continue;
            Folder folder = folderOpt.get();

            //delete and detach subfolders:
            if (folder.getSubFolders() != null && !folder.getSubFolders().isEmpty()){
                Set<Long> subFoldersIds = new HashSet<>();
                for (Folder subfolder : folder.getSubFolders())
                    subFoldersIds.add (subfolder.getId());

                deleteFoldersById (subFoldersIds);
            }

            //delete and detach photos:
            if (folder.getFilePhotos() != null && !folder.getFilePhotos().isEmpty()) {
                Set<FilePhoto> photos = folder.getFilePhotos();

                for (FilePhoto photo : photos)
                    photo.setFolder(null);

                folder.getFilePhotos().removeAll(photos);

                filePhotoRepository.deleteAll(photos);

            }

            //detach from parent
            if (folder.getParent() != null) {
                folder.getParent().getSubFolders().remove(folder);
                folder.setParent(null);
            }

            //detach from owners
            if (folder.getOwners() != null) {
                for (User user : folder.getOwners()){
                    user.getFolders().remove(folder);
                }
                folder.setOwners(null);
            }

            folderRepository.delete(folder);
        }
    }

    @Override
    public boolean createNewFolder(Long currentFolderId, String newFolderName) {
        Folder currentFolder = getFolderById(currentFolderId);
        if (currentFolder == null) {
            log.error("FolderService.createNewFolder(...).getFolderById() returned null. folderId = " + currentFolderId);
            return false;
        }

        currentFolder.addNewSubFolder (newFolderName);

        log.debug("FolderService.createNewFolder(): BEFORE REPO SAVE new folder id = " +
                currentFolder.getSubFolders().stream().filter((folder -> folder.getName().equals(newFolderName)))
                        .findFirst().orElse(new Folder(-1L)).getId()
        );

        folderRepository.save(currentFolder);

        log.debug("FolderService.createNewFolder(): AFTER REPO SAVE new folder id = " +
                currentFolder.getSubFolders().stream().filter((folder -> folder.getName().equals(newFolderName)))
                .findFirst().orElse(new Folder(-1L)).getId()
        );


        return true;
    }

    public boolean shareFolder (Long selectedFolderId, User newOwner) {

        if (newOwner == null){
            log.error("FolderService.shareFolder() newUser == NULL");
            return false;
        }

        Folder selectedFolder = getFolderById(selectedFolderId);
        if (selectedFolder == null) {
            log.error("FolderService.shareFolder(...).getFolderById() returned null. folderId = " + selectedFolderId);
            return false;
        }

        //если предлагаемый владелец и правда новый, а не один из старых, то сделать его действительным владельцем
        if (!selectedFolder.getOwners().contains(newOwner)) {
            selectedFolder.addOwner(newOwner);

            folderRepository.save(selectedFolder);
        }

        return true;
    }

    @Override
    public boolean shareSelectedFolders(Long currentFolderId, FoldersPhotosDTO foldersPhotosDTO, User newOwner) {

        if (newOwner == null){
            log.error("FolderService.shareSelectedFolders() newUser == NULL");
            return false;
        }

        //если были selected фото в текущей папке, то расшарим всю текущую папку
        if (foldersPhotosDTO.getPhotos()!= null && !foldersPhotosDTO.getPhotos().isEmpty()) {

            boolean isAnySelected = false;
            for (FilePhoto photo : foldersPhotosDTO.getPhotos())
                if (photo.getIsSelected()){
                    isAnySelected = true;
                    break;
                }

            if (isAnySelected)
                if (!shareFolder(currentFolderId, newOwner))
                    return false;
        }


        if (foldersPhotosDTO.getFolders()!=null && !foldersPhotosDTO.getFolders().isEmpty())
            for (Folder modelAttrFolder : foldersPhotosDTO.getFolders())
                if (modelAttrFolder.getIsSelected())
                    if (!shareFolder (modelAttrFolder.getId(), newOwner))
                        return false;

        if (foldersPhotosDTO.getSharedFolders()!=null && !foldersPhotosDTO.getSharedFolders().isEmpty())
            for (Folder modelAttrFolder : foldersPhotosDTO.getSharedFolders())
                if (modelAttrFolder.getIsSelected())
                    if (!shareFolder (modelAttrFolder.getId(), newOwner))
                        return false;

        userRepository.save(newOwner);

        return true;
    }

    @Override
    public boolean save(Folder folder) {
        if (folder == null)
            return false;

        Folder saved = folderRepository.save(folder);

        if (folder.equals(saved))
            return true;

        log.error("FolderServiceImpl.save() FAILED TO SAVE FOLDER IN REPOSITORY id = " + folder.getId () + " ; name = " + folder.getName());
        return false;
    }

}
