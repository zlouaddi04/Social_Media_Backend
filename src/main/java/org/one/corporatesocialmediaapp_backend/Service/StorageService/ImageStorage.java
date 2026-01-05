package org.one.corporatesocialmediaapp_backend.Service.StorageService;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorage {
    String uploadProfilePicture(MultipartFile file);

}
