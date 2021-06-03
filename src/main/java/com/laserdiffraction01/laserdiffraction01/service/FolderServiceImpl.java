package com.laserdiffraction01.laserdiffraction01.service;

import com.laserdiffraction01.laserdiffraction01.domain.Folder;
import com.laserdiffraction01.laserdiffraction01.domain.User;
import com.laserdiffraction01.laserdiffraction01.repository.FolderRepository;
import com.laserdiffraction01.laserdiffraction01.repository.UserRepository;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class FolderServiceImpl implements FolderService {
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;


    public FolderServiceImpl(UserRepository userRepository, FolderRepository folderRepository) {
        this.userRepository = userRepository;
        this.folderRepository = folderRepository;
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
}
