package com.cloudphotosapp.cloudphotosapp.repository;

import com.cloudphotosapp.cloudphotosapp.domain.Folder;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FolderRepository extends CrudRepository<Folder, Long> {
    Folder findByName(String name);
    List<Folder> findByNameContaining(String name);
}
