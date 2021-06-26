package com.cloudphotosapp.cloudphotosapp.service;


import java.util.List;

public interface CopyPasteService {
    void setCopied (List<Long> copiedPhotosIds,List<Long> copiedFoldersIds);
    boolean pasteCopied (Long currentFolderId);
}
