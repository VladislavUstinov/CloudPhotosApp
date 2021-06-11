package com.laserdiffraction01.laserdiffraction01.service;

import com.laserdiffraction01.laserdiffraction01.domain.Folder;
import javassist.NotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

public interface FolderService {

    public Folder getRootFolderByUsername (String username) throws UsernameNotFoundException;

    Folder getFolderById(Long id);

    void deleteFoldersById (Set<Long> folders);
}
