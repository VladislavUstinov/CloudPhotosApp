package com.cloudphotosapp.cloudphotosapp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class FilePhoto {

    private Boolean beingEdited = false;
    private Boolean isSelected = false;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id = UUID.randomUUID().getMostSignificantBits();

    private String name = "";

    private String contentType = "";

    //public List<String> getSomeList () { return Arrays.asList("abac", "tabac");}

    @Lob
    private Byte[] image = null;

    @ManyToOne
    private Folder folder = null;

    public static Byte[] deepCopyImage (Byte[] oldImage) {
        if (oldImage==null)
            return null;

        Byte[] newImage = new Byte[oldImage.length];
        for (int i = 0; i < oldImage.length;i++)
            newImage[i]=oldImage[i];

        return newImage;
    }

    public static FilePhoto deepCopyWithoutId (FilePhoto oldPhoto) {
        return new FilePhoto(oldPhoto.getName(), deepCopyImage (oldPhoto.getImage()), oldPhoto.getFolder());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilePhoto filePhoto = (FilePhoto) o;
        return Objects.equals(id, filePhoto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public FilePhoto(Long id, String name) {
        this.id = id;
        this.name = name;
        //this.contentType = contentType;
    }

    public void setImage(Byte[] image) {
        this.image = image;
    }

    public void setImageRawBytes(byte[] imageFileContent) {
        if (imageFileContent == null) {
            image = null;
            return;
        }

        image = new Byte [imageFileContent.length];

        for (int i = 0; i < imageFileContent.length; i ++)
            image [i] = imageFileContent [i];
    }

    public FilePhoto(String name, Byte[] image, Folder folder) {
        this.name = name;
        this.image = image;
        this.folder = folder;
    }

    public FilePhoto(Long id, String name, Byte[] image, Folder folder) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.folder = folder;
    }

    public FilePhoto(Long id, String name, byte[] imageFileContent) {
        this.id = id;
        this.name = name;
        setImageRawBytes(imageFileContent);
    }

    public FilePhoto(String name, byte[] imageFileContent, String contentType) {
        this.name = name;
        setImageRawBytes(imageFileContent);
        this.contentType = contentType;
    }
}
