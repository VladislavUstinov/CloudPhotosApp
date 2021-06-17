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

    public static int AMOUNT_OF_PREDEFIENED_SAMPLE_PHOTOS_ROOT_FOLDER = 30;
    public static String PREDEFINED_STATIC_PICTURE_EDIT_PEN = "staticPictureEditPen";

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

    public void loadStaticImageInFilePhotoRepository (String name, String path) {
        byte[] imageFileContent = null;

        File file = new File(path);
        try {
            imageFileContent = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            log.error("IMAGE FILE NOT FOUND when Bootstrap.onApplicationEvent.loadStaticImageInFilePhotoRepository\n");
            e.printStackTrace();
        }

        FilePhoto filePhoto = new FilePhoto (name, imageFileContent);
        filePhotoRepository.save(filePhoto);

        log.debug ("Have successfully loaded static image with name = " + name);
        log.debug ("filePhotoRepository.count() = " + filePhotoRepository.count());
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        loadStaticImageInFilePhotoRepository (PREDEFINED_STATIC_PICTURE_EDIT_PEN, "C:\\Users\\user\\IdeaProjects\\LaserDiffraction01\\src\\main\\resources\\static\\images\\pen.png");

        Role userRole = new Role(1L, Role.USER_ROLE_STRING);
        Role adminRole = new Role(2L, Role.ADMIN_ROLE_STRING);

        byte[] sampleImageFileContent1 = null;
        byte[] sampleImageFileContent2 = null;
        try {
            //todo - why normal source path doesn't work? "static/images/guacamole400x400.jpg"
            File file1 = new File("C:\\Users\\user\\IdeaProjects\\LaserDiffraction01\\src\\main\\resources\\static\\images\\guacamole400x400.jpg");
            sampleImageFileContent1 = Files.readAllBytes(file1.toPath());

            File file2 = new File("C:\\Users\\user\\IdeaProjects\\LaserDiffraction01\\src\\main\\resources\\static\\images\\tacos400x400.jpg");
            sampleImageFileContent2 = Files.readAllBytes(file2.toPath());

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

        log.debug ("filePhotoRepository.count() = " + filePhotoRepository.count());

        setFoldersAndPhotos (users, sampleImageFileContent1, sampleImageFileContent2);
    }

    private void setFoldersAndPhotos (List<User> users, byte[] sampleImageFileContent1, byte[] sampleImageFileContent2) {
        setFoldersAndPhotosSingleUser (0L, users.get(0), "0", sampleImageFileContent1, sampleImageFileContent2);
        setFoldersAndPhotosSingleUser (3L, users.get(1), "1", sampleImageFileContent1, sampleImageFileContent2);
        setFoldersAndPhotosSingleUser (6L, users.get(2), "2", sampleImageFileContent1, sampleImageFileContent2);

        log.debug("folderRepository.count() = " + folderRepository.count());
        log.debug("filePhotoRepository.count() = " + filePhotoRepository.count());
        log.debug("Have successfully loaded Bootstrap Data - Folders and Photos");

        /*log.debug("Full print of all photos in repository:");
        Iterator<FilePhoto> iter = filePhotoRepository.findAll().iterator();

        while (iter.hasNext()) {
            FilePhoto photo = iter.next();

            log.debug("name = " + photo.getName() + " ; id = " + photo.getId());
        }*/
    }

    private void setFoldersAndPhotosSingleUser (Long startIndexFolders, User user, String userNumber, byte[] sampleImageFileContent1, byte[] sampleImageFileContent2){
        //rootFolder0
        Folder rootFolder0 = new Folder(startIndexFolders+1L, "root"+userNumber);
        rootFolder0.addOwner (user);

        user.setRoot(rootFolder0);

        int amountPhotosInRoot = AMOUNT_OF_PREDEFIENED_SAMPLE_PHOTOS_ROOT_FOLDER;
        for (int i = 0; i < amountPhotosInRoot; i ++) {
            FilePhoto photoIndex0 = null;
            if (i % 2 == 0)
                photoIndex0 = new FilePhoto ("photo" + i + "" +userNumber, sampleImageFileContent1);
            else
                photoIndex0 = new FilePhoto ("photo" + i + "" +userNumber, sampleImageFileContent2);

            rootFolder0.addFilePhoto (photoIndex0);
        }

        //subFolder10
        Folder subFolder10 = new Folder (startIndexFolders+2L, "sub folder 1"+userNumber);
        rootFolder0.addSubFolder (subFolder10);
        subFolder10.addOwner (user);

        FilePhoto photo110 = new FilePhoto ("photo11"+userNumber, sampleImageFileContent1);
        FilePhoto photo210 = new FilePhoto ("photo21"+userNumber, sampleImageFileContent2);

        subFolder10.addFilePhoto (photo110);
        subFolder10.addFilePhoto (photo210);

        //subFolder20
        Folder subFolder20 = new Folder (startIndexFolders+3L, "sub folder 2"+userNumber);
        rootFolder0.addSubFolder (subFolder20);
        subFolder20.addOwner (user);

        FilePhoto photo120 = new FilePhoto ("photo12"+userNumber, sampleImageFileContent1);
        FilePhoto photo220 = new FilePhoto ("photo22"+userNumber, sampleImageFileContent2);

        subFolder20.addFilePhoto (photo120);
        subFolder20.addFilePhoto (photo220);

        folderRepository.save (rootFolder0);
        //folderRepository.save (subFolder10);
        //folderRepository.save (subFolder20);

        //filePhotoRepository.saveAll (rootPhotos);

        //filePhotoRepository.save (photo110);
        //filePhotoRepository.save (photo210);

        //filePhotoRepository.save (photo120);
        //filePhotoRepository.save (photo220);

        log.debug ("filePhotoRepository.count() = " + filePhotoRepository.count());
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

