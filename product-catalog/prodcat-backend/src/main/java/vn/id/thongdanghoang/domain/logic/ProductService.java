package vn.id.thongdanghoang.domain.logic;

import vn.id.thongdanghoang.domain.infra.persistence.entity.prodcat.ProductEntity;
import vn.id.thongdanghoang.domain.infra.persistence.repository.*;
import vn.id.thongdanghoang.domain.model.*;
import vn.id.thongdanghoang.domain.repository.model.*;
import vn.id.thongdanghoang.domain.repository.model.Paging.Page;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@WithTransaction()
public class ProductService {

  private final ProductRepositoryAdapter repository;
  private final ProductCategoryRepositoryAdapter productCategoryRepositoryAdapter;

  public Uni<UUID> createProduct(Product product) {
    return repository.create(product).map(Product::getId);
  }

  public Uni<Product> getProduct(UUID id) {
    return repository.getWithGraph(id, ProductEntity.ENTITY_GRAPH_NAME);
  }

  public Uni<Page<Product>> search(Filtering filtering, Sorting sorting, Paging paging) {
    return repository.findWithGraph(filtering, sorting, paging, ProductEntity.ENTITY_GRAPH_NAME);
  }

  public Uni<ProductCategory> createProductCategory(ProductCategory productCategory) {
    return productCategoryRepositoryAdapter.create(productCategory);
  }

  public Uni<Page<ProductCategory>> searchProductCategories(Filtering filtering, Sorting sorting,
      Paging paging) {
    return productCategoryRepositoryAdapter.findAll(filtering, sorting, paging);
  }
}
