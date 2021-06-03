package com.laserdiffraction01.laserdiffraction01.bootstrap;


import com.laserdiffraction01.laserdiffraction01.domain.FilePhoto;
import com.laserdiffraction01.laserdiffraction01.domain.Folder;
import com.laserdiffraction01.laserdiffraction01.domain.Role;
import com.laserdiffraction01.laserdiffraction01.domain.User;
import com.laserdiffraction01.laserdiffraction01.repository.FilePhotoRepository;
import com.laserdiffraction01.laserdiffraction01.repository.FolderRepository;
import com.laserdiffraction01.laserdiffraction01.repository.RoleRepository;
import com.laserdiffraction01.laserdiffraction01.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Component
public class LaserDiffractionBootStrap implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FolderRepository folderRepository;
    private final FilePhotoRepository filePhotoRepository;


    //private final BCryptPasswordEncoder bCryptPasswordEncoder;
    //@Autowired
    //BCryptPasswordEncoder bCryptPasswordEncoder;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder (PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public LaserDiffractionBootStrap(UserRepository userRepository, RoleRepository roleRepository, FolderRepository folderRepository, FilePhotoRepository filePhotoRepository){//}, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.folderRepository = folderRepository;
        this.filePhotoRepository = filePhotoRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Role userRole = new Role(1L, Role.USER_ROLE_STRING);
        Role adminRole = new Role(2L, Role.ADMIN_ROLE_STRING);

        List<User> users = getUsersWithRoleUser(userRole);

        userRepository.saveAll(users);
        roleRepository.save (userRole);

        userRepository.saveAll(getUsersWithRoleAdmin(adminRole));
        roleRepository.save (adminRole);

        log.debug("LaserDiffractionBootStrap.onApplicationEvent()");

        log.debug("userRepository.count() = " + userRepository.count());
        log.debug("Have successfully loaded Bootstrap Data - Users");


        setFoldersAndPhotos (users);
    }

    private void setFoldersAndPhotos (List<User> users) {
        setFoldersAndPhotosSingleUser (0L, 0L, users.get(0), "0");
        setFoldersAndPhotosSingleUser (3L, 6L, users.get(1), "1");

        log.debug("folderRepository.count() = " + folderRepository.count());
        log.debug("filePhotoRepository.count() = " + filePhotoRepository.count());
        log.debug("Have successfully loaded Bootstrap Data - Folders and Photos");
    }

    private void setFoldersAndPhotosSingleUser (Long startIndexFolders, Long startIndexPhotos, User user, String userNumber){
        //rootFolder0
        Folder rootFolder0 = new Folder(startIndexFolders+1L, "root"+userNumber);
        rootFolder0.addOwner (user);

        user.setRoot(rootFolder0);

        FilePhoto photo00 = new FilePhoto (startIndexPhotos+1L, "photo0"+userNumber);
        FilePhoto photo10 = new FilePhoto (startIndexPhotos+2L, "photo1"+userNumber);

        rootFolder0.addFilePhoto (photo00);
        rootFolder0.addFilePhoto (photo10);

        //subFolder10
        Folder subFolder10 = new Folder (startIndexFolders+2L, "sub folder 1"+userNumber);
        rootFolder0.addSubFolder (subFolder10);
        subFolder10.addOwner (user);

        FilePhoto photo110 = new FilePhoto (startIndexPhotos+3L, "photo11"+userNumber);
        FilePhoto photo210 = new FilePhoto (startIndexPhotos+4L, "photo21"+userNumber);

        subFolder10.addFilePhoto (photo110);
        subFolder10.addFilePhoto (photo210);

        //subFolder20
        Folder subFolder20 = new Folder (startIndexFolders+3L, "sub folder 2"+userNumber);
        rootFolder0.addSubFolder (subFolder20);
        subFolder20.addOwner (user);

        FilePhoto photo120 = new FilePhoto (startIndexPhotos+5L, "photo12"+userNumber);
        FilePhoto photo220 = new FilePhoto (startIndexPhotos+6L, "photo22"+userNumber);

        subFolder20.addFilePhoto (photo120);
        subFolder20.addFilePhoto (photo220);

        folderRepository.save (rootFolder0);
        folderRepository.save (subFolder10);
        folderRepository.save (subFolder20);

        filePhotoRepository.save (photo00);
        filePhotoRepository.save (photo10);

        filePhotoRepository.save (photo110);
        filePhotoRepository.save (photo210);

        filePhotoRepository.save (photo120);
        filePhotoRepository.save (photo220);
    }

    private List<User> getUsersWithRoleUser(Role userRole) {

        log.debug("LaserDiffractionBootStrap.getUsersWithRoleUser(Role userRole)");

        List<User> users = new ArrayList<>();
        users.add(new User("user0", "passw0", "passw0", userRole));
        users.add(new User("user1", "passw1", "passw1", userRole));
        users.add(new User("user2", "passw2", "passw2", userRole));

        log.debug("users.size() = " + users.size());

        userRole.setUsers(users);

        for (int i = 0; i < users.size(); i ++) {
            users.get(i).setPassword(passwordEncoder.encode(users.get(i).getPassword()));
            users.get(i).setPasswordConfirmation(passwordEncoder.encode(users.get(i).getPasswordConfirmation()));
        }

        return users;
    }

    private List<User> getUsersWithRoleAdmin(Role adminRole) {

        log.debug("LaserDiffractionBootStrap.getUsersWithRoleAdmin(Role adminRole)");

        List<User> users = new ArrayList<>();
        users.add(new User("admin", "admin", "admin", adminRole));

        log.debug("users.size() = " + users.size());

        adminRole.setUsers(users);

        for (int i = 0; i < users.size(); i ++) {
            users.get(i).setPassword(passwordEncoder.encode(users.get(i).getPassword()));
            users.get(i).setPasswordConfirmation(passwordEncoder.encode(users.get(i).getPasswordConfirmation()));
        }

        return users;
    }
}

