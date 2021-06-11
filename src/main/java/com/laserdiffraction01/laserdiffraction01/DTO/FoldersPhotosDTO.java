package com.laserdiffraction01.laserdiffraction01.DTO;

import com.laserdiffraction01.laserdiffraction01.domain.FilePhoto;
import com.laserdiffraction01.laserdiffraction01.domain.Folder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoldersPhotosDTO {
    private List<FilePhoto> photos = new ArrayList<>();
    private List<Folder> folders = new ArrayList<>();
    private FilePhoto staticPictureEditPen = new FilePhoto();
}
