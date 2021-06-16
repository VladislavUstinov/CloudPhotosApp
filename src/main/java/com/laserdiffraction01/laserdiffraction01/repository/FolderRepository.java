package com.laserdiffraction01.laserdiffraction01.repository;

import com.laserdiffraction01.laserdiffraction01.domain.Folder;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public interface FolderRepository extends CrudRepository<Folder, Long> {
    Folder findByName(String name);
    List<Folder> findByNameContaining(String name);
}
