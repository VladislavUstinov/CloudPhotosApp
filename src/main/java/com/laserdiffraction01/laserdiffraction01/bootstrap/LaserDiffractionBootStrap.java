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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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


        byte[] sampleImageFileContent = null;
        try {
            //todo - why normal source path doesn't work? "static/images/guacamole400x400.jpg"
            File file = new File("C:\\Users\\user\\IdeaProjects\\LaserDiffraction01\\src\\main\\resources\\static\\images\\guacamole400x400.jpg");
            sampleImageFileContent = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            log.error("IMAGE FILE NOT FOUND when Bootstrap.onApplicationEvent\n");
            e.printStackTrace();
        }

        List<User> users = getUsersWithRoleUser(userRole);

        userRepository.saveAll(users);
        roleRepository.save (userRole);

        userRepository.saveAll(getUsersWithRoleAdmin(adminRole));
        roleRepository.save (adminRole);

        log.debug("LaserDiffractionBootStrap.onApplicationEvent()");

        log.debug("userRepository.count() = " + userRepository.count());
        log.debug("Have successfully loaded Bootstrap Data - Users");


        setFoldersAndPhotos (users, sampleImageFileContent);
    }

    private void setFoldersAndPhotos (List<User> users, byte[] sampleImageFileContent) {
        setFoldersAndPhotosSingleUser (0L, 0L, users.get(0), "0", sampleImageFileContent);
        setFoldersAndPhotosSingleUser (3L, 6L, users.get(1), "1", sampleImageFileContent);

        log.debug("folderRepository.count() = " + folderRepository.count());
        log.debug("filePhotoRepository.count() = " + filePhotoRepository.count());
        log.debug("Have successfully loaded Bootstrap Data - Folders and Photos");
    }

    private void setFoldersAndPhotosSingleUser (Long startIndexFolders, Long startIndexPhotos, User user, String userNumber, byte[] sampleImageFileContent){
        //rootFolder0
        Folder rootFolder0 = new Folder(startIndexFolders+1L, "root"+userNumber);
        rootFolder0.addOwner (user);

        user.setRoot(rootFolder0);

        ArrayList<FilePhoto> rootPhotos = new ArrayList<>();
        int amountPhotosInRoot = 10;
        for (int i = 0; i < amountPhotosInRoot; i ++) {
            FilePhoto photoIndex0 = new FilePhoto (startIndexPhotos+1L+i, "photo" + i + "" +userNumber, sampleImageFileContent);
            rootFolder0.addFilePhoto (photoIndex0);
            rootPhotos.add(photoIndex0);
        }

        //subFolder10
        Folder subFolder10 = new Folder (startIndexFolders+2L, "sub folder 1"+userNumber);
        rootFolder0.addSubFolder (subFolder10);
        subFolder10.addOwner (user);

        FilePhoto photo110 = new FilePhoto (startIndexPhotos+amountPhotosInRoot+1L, "photo11"+userNumber, sampleImageFileContent);
        FilePhoto photo210 = new FilePhoto (startIndexPhotos+amountPhotosInRoot+2L, "photo21"+userNumber, sampleImageFileContent);

        subFolder10.addFilePhoto (photo110);
        subFolder10.addFilePhoto (photo210);

        //subFolder20
        Folder subFolder20 = new Folder (startIndexFolders+3L, "sub folder 2"+userNumber);
        rootFolder0.addSubFolder (subFolder20);
        subFolder20.addOwner (user);

        FilePhoto photo120 = new FilePhoto (startIndexPhotos+amountPhotosInRoot+3L, "photo12"+userNumber, sampleImageFileContent);
        FilePhoto photo220 = new FilePhoto (startIndexPhotos+amountPhotosInRoot+4L, "photo22"+userNumber, sampleImageFileContent);

        subFolder20.addFilePhoto (photo120);
        subFolder20.addFilePhoto (photo220);

        folderRepository.save (rootFolder0);
        folderRepository.save (subFolder10);
        folderRepository.save (subFolder20);

        filePhotoRepository.saveAll (rootPhotos);

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

