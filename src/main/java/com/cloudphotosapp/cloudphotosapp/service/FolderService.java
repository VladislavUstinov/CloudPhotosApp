package com.cloudphotosapp.cloudphotosapp.service;

import com.cloudphotosapp.cloudphotosapp.DTO.FoldersPhotosDTO;
import com.cloudphotosapp.cloudphotosapp.domain.FilePhoto;
import com.cloudphotosapp.cloudphotosapp.domain.Folder;
import com.cloudphotosapp.cloudphotosapp.domain.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Set;

public interface FolderService {

    public Folder getRootFolderByUsername (String username) throws UsernameNotFoundException;

    Folder getFolderById(Long id);

    void deleteFoldersById (Set<Long> folders);

    boolean createNewFolder(Long currentFolderId, String newFolderName);

    boolean shareSelectedFolders(Long currentFolderId, FoldersPhotosDTO foldersPhotosDTO, User newUser);

    public boolean save (Folder folder);

    public void search(List<Folder> resultFolders, List<FilePhoto> resultPhotos, String searchPhrase, User user) ;

    public Long getLeft (Long folderId, Long photoId);
    public Long getRight (Long folderId, Long photoId);
}
