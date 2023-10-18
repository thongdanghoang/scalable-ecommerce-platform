package fptu.swp391.shoppingcart.product.web.impl;

import fptu.swp391.shoppingcart.AbstractApplicationController;
import fptu.swp391.shoppingcart.product.dto.ProductDetailDto;
import fptu.swp391.shoppingcart.product.dto.ProductDto;
import fptu.swp391.shoppingcart.product.dto.ProductsResponse;
import fptu.swp391.shoppingcart.product.services.ImageService;
import fptu.swp391.shoppingcart.product.services.ProductService;
import fptu.swp391.shoppingcart.product.web.ProjectController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/products")
public class ProjectControllerImpl extends AbstractApplicationController implements ProjectController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ProductService productService;

    @GetMapping
    @Override
    public ResponseEntity<ProductsResponse> getAll(int page, int limit) {
        Page<ProductDto> products = productService.getAll(page, limit);
        return ResponseEntity.ok(new ProductsResponse(products.getContent(), products.getTotalElements()));
    }

    @Override
    public ResponseEntity<ProductsResponse> searchByKeyword(String keyword, int page, int limit) {
        return null;
    }

    @GetMapping("/search")
    @Override
    public ResponseEntity<ProductsResponse> search(String keyword, String sort, String category, String size, String colour,
                                                   int minPrice, int maxPrice, int page, int limit) {
        Page<ProductDto> result = productService.search(keyword, sort, category, size, colour, minPrice, maxPrice, page, limit);
        return ResponseEntity.ok(new ProductsResponse(result.getContent(), result.getTotalElements()));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<ProductDetailDto> findProductById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productService.findProductById(id));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }

    @GetMapping("/images/{fileName:.+}")
    @Override
    public ResponseEntity<Resource> serveImage(@PathVariable String fileName) throws IOException {
        Path filePath = imageService.getImagePath(fileName);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/*")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @PostMapping
//    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
//        String fileName = imageService.uploadImage(file);
//        String imageUrl = "/api/products/images/" + fileName;
//        return ResponseEntity.ok(imageUrl);
//    }
}
