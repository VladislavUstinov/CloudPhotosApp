package com.laserdiffraction01.laserdiffraction01.service;

import com.laserdiffraction01.laserdiffraction01.domain.Folder;
import javassist.NotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface FolderService {

    public Folder getRootFolderByUsername (String username) throws UsernameNotFoundException;

    Folder getFolderById(Long id);
}
