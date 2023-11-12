package fptu.swp391.shoppingcart.product.services;

import fptu.swp391.shoppingcart.product.dto.CategoryDto;
import fptu.swp391.shoppingcart.product.dto.ProductAddingDto;
import fptu.swp391.shoppingcart.product.dto.ProductDetailDto;
import fptu.swp391.shoppingcart.product.dto.ProductDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Page<ProductDto> getAll(int page, int limit);

    Page<ProductDto> searchByKeyword(int page, int limit, String keyword);

    ProductDetailDto findProductById(Long id);

    Page<ProductDto> search(String keyword, String sort, String category, String size, String colour, int minPrice, int maxPrice, int page, int limit);

    ProductAddingDto createProduct(ProductAddingDto productDto);

    public void deleteProductById(Long id);

    List<CategoryDto> getAllCategory();
}
