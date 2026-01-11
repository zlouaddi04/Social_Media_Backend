package org.one.corporatesocialmediaapp_backend.Service.StorageService;

import lombok.AllArgsConstructor;
import org.one.corporatesocialmediaapp_backend.Exceptions.StorageExceptions.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Profile("local")
@Service
public class LocalImageStorage implements ImageStorage{

    @Override
    public String uploadProfilePicture(MultipartFile file) {
        return store(file, "images/profile-pictures");
    }

    @Override
    public String uploadPostImage(MultipartFile file) {
        return store(file, "images/post-pictures");
    }

    private String store(MultipartFile file, String subFolder) {
        validate(file);

        try {
            Object properties;
            Path uploadPath = Paths.get("Uploads", subFolder);
            Files.createDirectories(uploadPath);

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path target = uploadPath.resolve(filename);

            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + subFolder + "/" + filename;

        } catch (IOException e) {
            throw new ImageUploadException("Failed to store image");
        }
    }

    private void validate(MultipartFile file) {
        if (file.isEmpty()) throw new InvalidImageException("Empty file");
        if (file.getSize() > 5_000_000) throw new InvalidImageException("File too large");
        if (!List.of("image/png","image/jpeg","image/webp")
                .contains(file.getContentType()))
            throw new InvalidImageException("Invalid type");
    }
}

