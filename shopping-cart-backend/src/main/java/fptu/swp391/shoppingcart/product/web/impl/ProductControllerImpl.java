package fptu.swp391.shoppingcart.product.web.impl;

import fptu.swp391.shoppingcart.AbstractApplicationController;
import fptu.swp391.shoppingcart.product.dto.*;
import fptu.swp391.shoppingcart.product.exceptions.ProductImageNotFoundException;
import fptu.swp391.shoppingcart.product.exceptions.ProductNotFoundException;
import fptu.swp391.shoppingcart.product.services.ImageService;
import fptu.swp391.shoppingcart.product.services.ProductService;
import fptu.swp391.shoppingcart.product.web.ProductController;
import fptu.swp391.shoppingcart.user.authentication.dto.ApiResponse;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/products")
public class ProductControllerImpl extends AbstractApplicationController implements ProductController {
    private final ImageService imageService;

    private final ProductService productService;

    public ProductControllerImpl(ImageService imageService, ProductService productService) {
        this.imageService = imageService;
        this.productService = productService;
    }

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

    @PostMapping
    @Override
    public ResponseEntity<ProductAddingDto> createProduct(ProductAddingDto productDto) {
        try {
            return ResponseEntity.ok(productService.createProduct(productDto));
        } catch (ProductImageNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    @PutMapping
    public ResponseEntity<ProductAddingDto> updateProduct(ProductAddingDto productDto) {
        try {
            return ResponseEntity.ok(productService.update(productDto));
        } catch (ProductImageNotFoundException | ProductNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/report")
    @Override
    public ResponseEntity<ApiResponse<List<ReportResponseDto>>> report() {
        return ResponseEntity.ok(new ApiResponse<>("Report generated successfully", true, productService.report()));
    }

    @Override
    @GetMapping("/statistic")
    public ResponseEntity<ApiResponse<StatisticProductDto>> statistic() {
        return ResponseEntity.ok(new ApiResponse<>("Statistic generated successfully", true, productService.statistic()));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(productService.getAllCategory());
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

    @PostMapping("/image/upload")
    public ApiResponse<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            String fileName = imageService.uploadImage(file);
            String imageUrl = "/api/products/images/" + fileName;
            return new ApiResponse<>("Image uploaded successfully", true, imageUrl);
        } catch (FileAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
