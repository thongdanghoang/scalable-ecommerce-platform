package fptu.swp391.shoppingcart.product.repo.custom;

import fptu.swp391.shoppingcart.product.dto.CategorySoldReportDTO;
import fptu.swp391.shoppingcart.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {
    // sort: popular, lowest price, highest price, asc, desc, newest, oldest
    // keyword: product name
    // category: category name
    // minPrice, maxPrice: price range
    // page, limit: pagination

    Page<Product> search(String keyword, String sort, String category, String size,String colour,
                         int minPrice, int maxPrice,
                         Pageable pageable);
}
