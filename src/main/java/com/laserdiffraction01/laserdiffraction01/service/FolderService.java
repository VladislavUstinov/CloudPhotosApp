package com.laserdiffraction01.laserdiffraction01.service;

import com.laserdiffraction01.laserdiffraction01.DTO.FoldersPhotosDTO;
import com.laserdiffraction01.laserdiffraction01.domain.FilePhoto;
import com.laserdiffraction01.laserdiffraction01.domain.Folder;
import com.laserdiffraction01.laserdiffraction01.domain.User;
import javassist.NotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
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
