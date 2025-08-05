package edu.fpt.customflorist.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/api/v1/images")
@CrossOrigin(origins = {"*", "http://localhost:3000", "https://yourflorist.vercel.app"})
public class ImageController {

    private final Cloudinary cloudinary;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                         @RequestParam("folder") String folder) {
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                return ResponseEntity.badRequest().body("File name is missing");
            }

            // Optional: Loại bỏ extension khỏi tên file nếu muốn
            String filenameWithoutExt = originalFilename.replaceAll("\\.[^.]+$", "");
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);

            // Tạo public_id từ tên file gốc (nên thêm UUID để tránh trùng lặp)
            String publicId = folder + "/" + filenameWithoutExt + "-" + UUID.randomUUID();

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "public_id", publicId,
                    "folder", folder,
                    "resource_type", "auto"
            ));

            String imageUrl = uploadResult.get("secure_url").toString();
            return ResponseEntity.ok(imageUrl);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed: " + e.getMessage());
        }
    }


    @GetMapping("/{folder}/{filename}")
    public ResponseEntity<String> getImageUrl(@PathVariable String folder, @PathVariable String filename) {
        String publicId = folder + "/" + filename;
        String imageUrl = cloudinary.url().secure(true).generate(publicId);
        return ResponseEntity.ok(imageUrl);
    }
}
