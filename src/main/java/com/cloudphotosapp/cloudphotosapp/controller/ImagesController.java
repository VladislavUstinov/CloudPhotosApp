package com.cloudphotosapp.cloudphotosapp.controller;

import com.cloudphotosapp.cloudphotosapp.customexception.CustomExceptionAccessDenied;
import com.cloudphotosapp.cloudphotosapp.domain.FilePhoto;
import com.cloudphotosapp.cloudphotosapp.domain.User;
import com.cloudphotosapp.cloudphotosapp.repository.FilePhotoRepository;
import com.cloudphotosapp.cloudphotosapp.service.ImageService;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class ImagesController {
    private final FilePhotoRepository filePhotoRepository;
    private final ImageService imageService;

    public ImagesController(FilePhotoRepository filePhotoRepository, ImageService imageService) {
        this.filePhotoRepository = filePhotoRepository;
        this.imageService = imageService;
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

    @PostMapping(value = "photos/{photoId}/download_image")
    public ResponseEntity<byte[]> downloadImage(@AuthenticationPrincipal User activeUser, @PathVariable String photoId) throws Exception { // (1) Return byte array response

        Optional<FilePhoto> filePhotoOptional = filePhotoRepository.findById(Long.valueOf(photoId));

        if (filePhotoOptional.isPresent()) {
            FilePhoto filePhoto = filePhotoOptional.get();

            if (filePhoto.getFolder() == null || filePhoto.getFolder().getOwners() == null || filePhoto.getImage() == null || filePhoto.getImage().length == 0) {
                log.error("ImagesController.downloadImage(photoId="+ photoId + "): ERROR - filePhoto.getFolder() == null || filePhoto.getFolder().getOwners() == null || filePhoto.getImage() == null || filePhoto.getImage().length == 0");
                return ResponseEntity.noContent().build();
            }

            if (!filePhoto.getFolder().getOwners().contains(activeUser)){
                log.debug("ImagesController.downloadImage(): ERROR - USER " + activeUser.getUsername() + " HAS NO RIGHT TO DOWNLOAD PHOTO id = " + photoId);
                throw new CustomExceptionAccessDenied("ImagesController.downloadImage(): ERROR - USER " + activeUser.getUsername() + " HAS NO RIGHT TO DOWNLOAD PHOTO id = " + photoId);
            }

            byte[] byteArray = new byte[filePhoto.getImage().length];
            int i = 0;

            for (Byte wrappedByte : filePhoto.getImage()) {
                byteArray[i++] = wrappedByte; //auto unboxing
            }

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE);
            //todo not just add ".jpg" to filename!! Better would be to store extension and add it
            if (filePhoto.getName().contains("."))
                httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(filePhoto.getName()).build().toString());
            else
                httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename(filePhoto.getName()+".jpg").build().toString());

            return ResponseEntity.ok().headers(httpHeaders).body(byteArray);
        }

        log.error("ImagesController.downloadImage(photoId="+ photoId + "): ERROR - filePhoto NOT FOUND");
        throw new NotFoundException ("ImagesController.downloadImage(photoId="+ photoId + "): ERROR - filePhoto NOT FOUND");
    }

    @PostMapping("photos/{folderId}/imageuploadform/")
    public String handleImagePost(@PathVariable String folderId, @RequestParam("imagefile") List<MultipartFile> files){

        for (MultipartFile file : files) {
            log.debug("ContentController.handleImagePost. Image name = " + file.getOriginalFilename());
            if (file.getOriginalFilename().isEmpty() == false)
                imageService.saveImageFile(Long.valueOf(folderId), file);

        }
        return "redirect:/photos/" + folderId;
    }
}
