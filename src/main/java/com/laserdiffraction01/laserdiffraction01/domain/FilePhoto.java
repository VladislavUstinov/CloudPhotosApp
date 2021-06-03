package com.laserdiffraction01.laserdiffraction01.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FilePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id = UUID.randomUUID().getMostSignificantBits();

    private String name = "";

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
}
