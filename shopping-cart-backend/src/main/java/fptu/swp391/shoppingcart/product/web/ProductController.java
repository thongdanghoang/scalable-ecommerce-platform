package fptu.swp391.shoppingcart.product.web;

import fptu.swp391.shoppingcart.product.dto.*;
import fptu.swp391.shoppingcart.user.authentication.dto.ApiResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

public interface ProductController {

    ResponseEntity<ProductsResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "30") int limit);

    ResponseEntity<ProductsResponse> searchByKeyword(@RequestParam String keyword, // pagination search by keyword
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "30") int limit);
    // TODO : product detail field in this api
    ResponseEntity<ProductDetailDto> findProductById(@PathVariable Long id);

    ResponseEntity<Resource> serveImage(@PathVariable String fileName) throws IOException;

    // combine search, filter, sort to one api,
    ResponseEntity<ProductsResponse> search(@RequestParam(required = false) String keyword,
                                            @RequestParam(required = false) String sort,
                                            @RequestParam(required = false) String category,
                                            @RequestParam(required = false) String size,
                                            @RequestParam(required = false) String colour,
                                            @RequestParam(defaultValue = "-1") int minPrice,
                                            @RequestParam(defaultValue = "-1") int maxPrice,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "30") int limit);

    // TODO: add product detail field in this api
    ResponseEntity<ProductAddingDto> createProduct(@RequestBody ProductAddingDto productDto);

    ResponseEntity<ProductAddingDto> updateProduct(@RequestBody ProductAddingDto productDto);

    ResponseEntity<ApiResponse<List<ReportResponseDto>>> report();

    ResponseEntity<ApiResponse<StatisticProductDto>> statistic();
}
