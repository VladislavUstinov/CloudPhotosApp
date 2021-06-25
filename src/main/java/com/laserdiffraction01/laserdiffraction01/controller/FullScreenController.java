package com.laserdiffraction01.laserdiffraction01.controller;

import com.laserdiffraction01.laserdiffraction01.DTO.FoldersPhotosDTO;
import com.laserdiffraction01.laserdiffraction01.service.FolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class FullScreenController {

    private final FolderService folderService;

    public FullScreenController(FolderService folderService) {
        this.folderService = folderService;
    }

    ////////////// working with main buttons in photos/
    ////////////// work with full screen

    @PostMapping("photos/goFullScreen/currentFolder/{currentFolderId}")
    public String goFullScreenNoSelectedPhoto (@ModelAttribute("foldersPhotosDTO") FoldersPhotosDTO foldersPhotosDTO, @PathVariable("currentFolderId") String currentFolderId,
                                               Model model){

        if (foldersPhotosDTO.getPhotos()!=null && !foldersPhotosDTO.getPhotos().isEmpty()) {
            model.addAttribute("selectedPhotoId", foldersPhotosDTO.getPhotos().iterator().next().getId());
            model.addAttribute("currentFolderId", currentFolderId);
            model.addAttribute("foldersPhotosDTO", foldersPhotosDTO);
        } else{
            model.addAttribute("NoPhotosAvailable", true);
        }

        return "fullscreen";
    }

    @PostMapping("photos/goFullScreen/currentFolder/{currentFolderId}/selectedPhoto/{selectedPhotoId}")
    public String goFullScreen (@ModelAttribute("foldersPhotosDTO") FoldersPhotosDTO foldersPhotosDTO, @PathVariable("currentFolderId") String currentFolderId,
                                @PathVariable("selectedPhotoId") String selectedPhotoId, Model model){

        model.addAttribute("selectedPhotoId", selectedPhotoId);
        model.addAttribute("currentFolderId", currentFolderId);
        model.addAttribute("foldersPhotosDTO", foldersPhotosDTO);

        return "fullscreen";
    }

    @PostMapping("photos/goFullScreen/currentFolder/{currentFolderId}/selectedPhoto/{selectedPhotoId}/left")
    public String goFullScreenLeft (@ModelAttribute("foldersPhotosDTO") FoldersPhotosDTO foldersPhotosDTO, @PathVariable("currentFolderId") String currentFolderId,
                                    @PathVariable("selectedPhotoId") String selectedPhotoId, Model model){

        Long leftId = folderService.getLeft (Long.valueOf(currentFolderId), Long.valueOf(selectedPhotoId));

        if (leftId == null) {
            model.addAttribute("NoNextPhotoAvailable", true);
            log.error("ContentController.goFullScreenLeft(): folderService.getLeft returned NULL when " +
                    "currentFolderId = " + currentFolderId + " ; selectedPhotoId = " + selectedPhotoId);
            return "fullscreen";
        }

        model.addAttribute("selectedPhotoId", leftId);
        model.addAttribute("currentFolderId", currentFolderId);
        model.addAttribute("foldersPhotosDTO", foldersPhotosDTO);

        return "fullscreen";
    }

    @PostMapping("photos/goFullScreen/currentFolder/{currentFolderId}/selectedPhoto/{selectedPhotoId}/right")
    public String goFullScreenRight (@ModelAttribute("foldersPhotosDTO") FoldersPhotosDTO foldersPhotosDTO, @PathVariable("currentFolderId") String currentFolderId,
                                     @PathVariable("selectedPhotoId") String selectedPhotoId, Model model){

        Long rightId = folderService.getRight (Long.valueOf(currentFolderId), Long.valueOf(selectedPhotoId));

        if (rightId == null) {
            model.addAttribute("NoNextPhotoAvailable", true);
            log.error("ContentController.goFullScreenRight(): folderService.getRight returned NULL when " +
                    "currentFolderId = " + currentFolderId + " ; selectedPhotoId = " + selectedPhotoId);
            return "fullscreen";
        }

        model.addAttribute("selectedPhotoId", rightId);
        model.addAttribute("currentFolderId", currentFolderId);
        model.addAttribute("foldersPhotosDTO", foldersPhotosDTO);

        return "fullscreen";
    }

}
