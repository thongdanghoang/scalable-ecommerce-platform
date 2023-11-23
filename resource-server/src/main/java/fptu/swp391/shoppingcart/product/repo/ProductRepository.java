package fptu.swp391.shoppingcart.product.repo;

import fptu.swp391.shoppingcart.product.entity.Product;
import fptu.swp391.shoppingcart.product.repo.custom.ProductRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ProductRepository extends JpaRepository<Product, Long>,
        QuerydslPredicateExecutor<Product>, ProductRepositoryCustom {
    Page<Product> findAll(Pageable pageable );
}
