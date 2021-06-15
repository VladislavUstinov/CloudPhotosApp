package com.laserdiffraction01.laserdiffraction01.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = UUID.randomUUID().getMostSignificantBits();

    private String name = "";

    private Boolean isSelected = false;
    private Boolean beingEdited = false;

    //пользователи - владельцы папки. В будущем можно несколько вариантов, у кого какие права доступа
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> owners = new HashSet<>();

    //папка является обычно и подпапкой, так что есть предок дерева папок. Только в корне parent=null
    @ManyToOne
    private Folder parent = null;

    //у папки обычно есть подпапки. Если она лист, то множество, хотя и не null, но пустое
    @OneToMany(cascade=CascadeType.ALL,mappedBy="parent")
    private Set<Folder> subFolders = new HashSet<>();

    //всякая папка может хранить пустой или непустой набор файлов. В данном случае фотографий
    @OneToMany(cascade=CascadeType.ALL,mappedBy="folder")
    private Set<FilePhoto> filePhotos = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Folder folder = (Folder) o;
        return Objects.equals(id, folder.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Folder(String name) {
        this.name = name;
    }

    public Folder(Long id) {
        this.id = id;
    }

    public Folder(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addNewSubFolder (String newFolderName){
        Folder newFolder = new Folder(newFolderName);

        newFolder.setParent(this);

        this.getSubFolders().add(newFolder);
        newFolder.getOwners().addAll(this.getOwners());

        for (User owner : newFolder.getOwners())
            owner.getFolders().add(newFolder);
    }

    public void addOwner (User owner){
        owners.add(owner);
        owner.getFolders().add(this);
    }

    public void addFilePhoto (FilePhoto photo){
        filePhotos.add(photo);
        photo.setFolder(this);
    }
    public void addSubFolder (Folder subFolder){
        subFolders.add(subFolder);
        subFolder.setParent(this);
    }

}
