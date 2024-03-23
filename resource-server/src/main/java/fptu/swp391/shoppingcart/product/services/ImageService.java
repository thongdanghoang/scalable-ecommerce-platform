package fptu.swp391.shoppingcart.product.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;

@Service
public class ImageService {
    private final Path imageUploadPath;

    @Autowired
    private RestTemplate restTemplate;

    public ImageService(@Value("${products-image-dir}") String uploadDir) {
        this.imageUploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.imageUploadPath);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the directory for image uploads.");
        }
    }

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Path targetLocation = this.imageUploadPath.resolve(fileName);

        if (Files.exists(targetLocation)) {
            throw new FileAlreadyExistsException("File with the same name already exists: " + fileName);
        } else {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        }
    }

    public String uploadImageFromUrl(String imageUrl) throws IOException {
        // Generate a unique filename
        String fileName = UUID.randomUUID().toString();

        // Create a final array to store the file extension
        final String[] fileExtension = { "jpg" };  // Default to jpg

        restTemplate.execute(imageUrl, HttpMethod.GET, null, clientHttpResponse -> {
            String contentType = Objects.requireNonNull(clientHttpResponse.getHeaders().getContentType()).getSubtype();
            if (!contentType.isEmpty()) {
                fileExtension[0] = contentType;
            }

            // Save the image with the determined filename and extension
            Path targetLocation = this.imageUploadPath.resolve(fileName + "." + fileExtension[0]);
            Files.copy(clientHttpResponse.getBody(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return null;
        });

        return fileName + "." + fileExtension[0];
    }


    public Path getImagePath(String fileName) {
        return this.imageUploadPath.resolve(fileName).normalize();
    }

    public Resource getImageResource(String fileName) throws IOException {
        Path filePath = getImagePath(fileName);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new FileNotFoundException("Image not found: " + fileName);
        }
    }
    public boolean isImageExist(String fileName) {
        Path imagePath = getImagePath(fileName);
        return Files.exists(imagePath);
    }
}
