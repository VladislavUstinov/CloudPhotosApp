package com.cloudphotosapp.cloudphotosapp.service;

import com.cloudphotosapp.cloudphotosapp.domain.Folder;
import com.cloudphotosapp.cloudphotosapp.domain.Role;
import com.cloudphotosapp.cloudphotosapp.domain.User;
import com.cloudphotosapp.cloudphotosapp.DTO.UserChangePasswordDTO;
import com.cloudphotosapp.cloudphotosapp.repository.FolderRepository;
import com.cloudphotosapp.cloudphotosapp.repository.RoleRepository;
import com.cloudphotosapp.cloudphotosapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FolderRepository folderRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder (PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // todo where should I put @Transactional


    @Transactional
    @Override
    public List<User> findAll() {
        log.debug("I'm in the UserServiceImpl - findAll()");

        //todo - do I really need to make deep copy? Although they are only references, it's not that expensive..
        List<User> userList = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(userList::add);

        return userList;
    }

    @Transactional
    @Override
    public boolean updateUser(User userForm, String roleName) {
        ArrayList<String> roleNames = new ArrayList<>();
        roleNames.add (roleName);
        return updateUser(userForm, roleNames);
    }

    //todo - rename update into addNew!!!
    @Transactional
    @Override
    public boolean updateUser(User userForm, ArrayList<String> roleNames) {
        if (userForm == null)
            return false;

        if (userRepository.findByUsername(userForm.getUsername()) != null)
            return false;

        //todo может переставить это дело в контроллер?
        userForm.setPassword(passwordEncoder.encode(userForm.getPassword()));
        userForm.setPasswordConfirmation(passwordEncoder.encode(userForm.getPasswordConfirmation()));

        if (userForm.getRoles() == null)
            userForm.setRoles (new HashSet<>());

        for (int i = 0; i < roleNames.size(); i ++) {
            Role role = roleRepository.findByName(roleNames.get(i));
            if (role != null) {
                role.getUsers().add(userForm);
                userForm.getRoles().add (role);
            } else
                log.debug("in UserServiceImpl.saveUser(...): roleRepository.findByName(" +
                        roleNames.get(i) + ") returned null");
        }

        User newUser = userRepository.save(userForm);

        if(newUser.getRoot()==null) {
            Folder newRoot = new Folder();
            newRoot.setId(null);
            newRoot.setName("Root " + newUser.getUsername());
            newRoot.addOwner(newUser);
            newUser.setRoot(newRoot);
            folderRepository.save(newRoot);
            String abac = "abac";
        }

        return true;
    }

    @Override
    public void deleteUserByUsername(String username) throws UsernameNotFoundException {
        User user = (User) userRepository.findByUsername(username);

        if (user == null) {
            log.error("UserServiceImpl.deleteUserByUsername(): USER NOT FOUND, username is " + username);
            throw new UsernameNotFoundException("User {" + username + "} not found");
        }

        //delete folders that are owned solely by this User, detach all his folders from his ownership,

        Folder root = user.getRoot();
        user.setRoot(null);

        //user will not own anything
        for (Folder folder : user.getFolders())
            folder.getOwners().remove(user);

        //delete folder tree starting from root, except for the branches shared with other users
        if (root != null)
            deleteWithSubFoldersUntilTheyHaveSingleUser(user, root);

        //user will not have access to anything
        if (user.getFolders() != null)
            user.getFolders().removeAll(user.getFolders());

        userRepository.deleteById(user.getId());
    }

    @Override
    public boolean checkPasswordsAndSave(UserChangePasswordDTO userForm) {
        if (userForm == null) {
            log.error("UserServiceImpl.checkPasswordsAndSave(): userForm == null");
            return false;
        }

        if (!userForm.getNewPassword().equals(userForm.getNewPasswordConfirmed())) {
            log.error("UserServiceImpl.checkPasswordsAndSave(): new passwords not equal to its confirmation when username = " + userForm.getUsername());
            return false;
        }

        User existingUser = userRepository.findByUsername(userForm.getUsername());

        if (existingUser == null) {
            log.error("UserServiceImpl.checkPasswordsAndSave(): USER NOT FOUND when username = " + userForm.getUsername());
            return false;
        }

        if (!passwordEncoder.matches(userForm.getOldPassword(), existingUser.getPassword())){
            log.error("UserServiceImpl.checkPasswordsAndSave(): old password mismatch when username = " + userForm.getUsername());
            return false;
        }

        existingUser.setPassword(passwordEncoder.encode(userForm.getNewPassword()));
        userRepository.save(existingUser);

        return true;
    }

    private void deleteWithSubFoldersUntilTheyHaveSingleUser (User user, Folder folder){

            if (folder.getOwners().size() == 0){
                if (folder.getParent() != null) {
                    folder.getParent().getSubFolders().remove(folder);
                    folder.setParent(null);
                }

                while (folder.getSubFolders() != null && folder.getSubFolders().iterator().hasNext()) {
                    Folder subFolder = folder.getSubFolders().iterator().next();
                    deleteWithSubFoldersUntilTheyHaveSingleUser(user, subFolder);
                }

                user.getFolders().remove(folder);
                //do I need to delete folder.getFilePhotos() - no, it's ok
                folderRepository.delete(folder);
            } else {
                if (folder.getParent() != null && folder.getParent().getOwners().size() == 0) {
                    folder.getParent().getSubFolders().remove(folder);
                    folder.setParent(null);
                }
            }

    }

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, FolderRepository folderRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.folderRepository = folderRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User {" + username + "} not found");
        }

        return user;
    }
}




/*        System.out.println ("for through userRepository.findAll().iterator() in UserService.findAll()");

        for ( Iterator<User> userIterator = userRepository.findAll().iterator() ;
              userIterator.hasNext(); ) {

            User user = userIterator.next();
            System.out.println (user.getName());
        }*/