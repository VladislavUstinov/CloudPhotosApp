package com.cloudphotosapp.cloudphotosapp.controller;

import com.cloudphotosapp.cloudphotosapp.domain.FilePhoto;
import com.cloudphotosapp.cloudphotosapp.repository.FilePhotoRepository;
import com.cloudphotosapp.cloudphotosapp.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
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

    ////////////// working with images

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
