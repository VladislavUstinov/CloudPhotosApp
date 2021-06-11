package com.laserdiffraction01.laserdiffraction01.service;

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

}
