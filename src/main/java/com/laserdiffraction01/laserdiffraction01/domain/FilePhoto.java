package com.laserdiffraction01.laserdiffraction01.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class FilePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id = UUID.randomUUID().getMostSignificantBits();

    private String name = "";

    //public List<String> getSomeList () { return Arrays.asList("abac", "tabac");}

    @Lob
    private Byte[] image = null;

    @ManyToOne
    private Folder folder = null;

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
}
