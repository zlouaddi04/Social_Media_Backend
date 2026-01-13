package org.one.corporatesocialmediaapp_backend.Service.StorageService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.one.corporatesocialmediaapp_backend.Exceptions.StorageExceptions.ImageUploadException;
import org.one.corporatesocialmediaapp_backend.Exceptions.StorageExceptions.InvalidImageException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Profile("cloudinary")
@Service
public class CloudImageStorage implements ImageStorage{
    private final Cloudinary cloudinary;

    @Override
    public String uploadProfilePicture(MultipartFile file) {

        validateImage(file);

        try {
            Map result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "users/profile-pictures",
                            "resource_type", "image"
                    )
            );

            return result.get("secure_url").toString();

        } catch (IOException e) {
            throw new ImageUploadException("Failed to upload image");
        }
    }

    @Override
    public String uploadPostImage(MultipartFile file) {
        validateImage(file);

        try {
            Map result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "posts/post-pictures",
                            "resource_type", "image"
                    )
            );

            return result.get("secure_url").toString();

        } catch (IOException e) {
            throw new ImageUploadException("Failed to upload post image");
        }
    }

    private void validateImage(MultipartFile file) {
        if (file.getSize() > 2_000_000)
            throw new InvalidImageException("Image too large");

        if (!List.of("image/png", "image/jpeg", "image/webp")
                .contains(file.getContentType()))
            throw new InvalidImageException("Invalid image type");
    }
}
