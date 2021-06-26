package com.cloudphotosapp.cloudphotosapp.DTO;

import com.cloudphotosapp.cloudphotosapp.domain.FilePhoto;
import com.cloudphotosapp.cloudphotosapp.domain.Folder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoldersPhotosDTO {
    private List<FilePhoto> photos = new ArrayList<>();
    private List<Folder> folders = new ArrayList<>();
    private List<Folder> sharedFolders = new ArrayList<>();
    private FilePhoto staticPictureEditPen = new FilePhoto();
    private FilePhoto staticPictureArrowDown = new FilePhoto();
    private String newFolderName = "";
    private String newOwnerName = "";
    private String searchPhrase = "";

    public FoldersPhotosDTO(List<FilePhoto> photos, List<Folder> folders, List<Folder> sharedFolders, FilePhoto staticPictureEditPen, FilePhoto staticPictureArrowDown) {
        this.photos = photos;
        this.folders = folders;
        this.staticPictureEditPen = staticPictureEditPen;
        this.staticPictureArrowDown = staticPictureArrowDown;
        this.sharedFolders=sharedFolders;
    }
}
