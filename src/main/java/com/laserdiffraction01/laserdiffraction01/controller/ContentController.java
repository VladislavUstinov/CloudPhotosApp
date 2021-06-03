package com.laserdiffraction01.laserdiffraction01.controller;

import com.laserdiffraction01.laserdiffraction01.domain.FilePhoto;
import com.laserdiffraction01.laserdiffraction01.domain.Folder;
import com.laserdiffraction01.laserdiffraction01.domain.User;
import com.laserdiffraction01.laserdiffraction01.repository.FilePhotoRepository;
import com.laserdiffraction01.laserdiffraction01.repository.RoleRepository;
import com.laserdiffraction01.laserdiffraction01.service.FolderService;
import com.laserdiffraction01.laserdiffraction01.service.ImageService;
import com.laserdiffraction01.laserdiffraction01.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Slf4j
@Controller
public class ContentController {
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final FolderService folderService;
    private final ImageService imageService;
    private final FilePhotoRepository filePhotoRepository;

    public ContentController(UserService userService, RoleRepository roleRepository, FolderService folderService, ImageService imageService, FilePhotoRepository filePhotoRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.folderService = folderService;
        this.imageService = imageService;
        this.filePhotoRepository = filePhotoRepository;
    }

    @GetMapping("/index")
    String getIndex(Model model){

        return "index";
    }

    @GetMapping("/photos/{folderId}")
    String getCurrentFolder(@PathVariable String folderId, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        Folder folder = folderService.getFolderById(Long.decode(folderId));

        if (folder == null) {
            return "photos";//todo add error
        }

        User user = (User) userService.loadUserByUsername(currentPrincipalName);

        if (folder.getOwners().contains(user) == false) {
            return "photos";//todo add error
        }

        model.addAttribute("parent", folder.getParent());
        model.addAttribute("folders", folder.getSubFolders());
        model.addAttribute("photos", folder.getFilePhotos());
        model.addAttribute("currentFolder", folder);

        return "photos";
    }


    @GetMapping("/photos")
    String getPhotosAndUsersRoot(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        Folder root = folderService.getRootFolderByUsername (currentPrincipalName);

        if (root == null)
            return "photos";//todo add error

        model.addAttribute("folders", root.getSubFolders());
        model.addAttribute("photos", root.getFilePhotos());
        model.addAttribute("currentFolder", root);

        return "photos";
    }

    ////////////// working with images

    @PostMapping("photos/{folderId}/imageuploadform/")
    public String handleImagePost(@PathVariable String folderId, @RequestParam("imagefile") MultipartFile file){

        imageService.saveImageFile(Long.valueOf(folderId), file);

        return "redirect:/photos/" + folderId;
    }

    @GetMapping("photos/currentFolder/{currentFolderId}/selectphoto/{selectedPhotoId}")
    public String selectPhoto (@PathVariable String currentFolderId, @PathVariable String selectedPhotoId, Model model) {
        Optional<FilePhoto> filePhotoOptional = filePhotoRepository.findById(Long.valueOf(selectedPhotoId));

        if (filePhotoOptional.isPresent()) {
            FilePhoto filePhoto = filePhotoOptional.get();
            model.addAttribute("selectedFilePhoto", filePhoto);

            getCurrentFolder(currentFolderId, model);

            log.debug("ContentController.selectPhoto(), photoName = " + filePhoto.getName());
            return "photos";
        }
        else {
            //todo add error
            return "photos";
        }
    }

    @GetMapping("photos/{photoId}/image")
    public void renderImageFromDB(@PathVariable String photoId, HttpServletResponse response) throws IOException {

        Optional<FilePhoto> filePhotoOptional = filePhotoRepository.findById(Long.valueOf(photoId));

        if (filePhotoOptional.isPresent()) {
            FilePhoto filePhoto = filePhotoOptional.get();

            if (filePhoto.getImage() == null || filePhoto.getImage().length == 0)
                return;

            byte[] byteArray = new byte[filePhoto.getImage().length];
            int i = 0;

            for (Byte wrappedByte : filePhoto.getImage()){
                byteArray[i++] = wrappedByte; //auto unboxing
            }

            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(byteArray);
            IOUtils.copy(is, response.getOutputStream());
        }

    }
}
