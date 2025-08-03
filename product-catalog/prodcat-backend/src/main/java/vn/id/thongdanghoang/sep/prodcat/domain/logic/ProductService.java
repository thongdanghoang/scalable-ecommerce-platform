package vn.id.thongdanghoang.sep.prodcat.domain.logic;

import vn.id.thongdanghoang.sep.prodcat.domain.infra.persistence.repository.ProductCategoryRepositoryAdapter;
import vn.id.thongdanghoang.sep.prodcat.domain.infra.persistence.repository.ProductRepositoryAdapter;
import vn.id.thongdanghoang.sep.prodcat.domain.model.Product;
import vn.id.thongdanghoang.sep.prodcat.domain.model.ProductCategory;
import vn.id.thongdanghoang.sep.prodcat.domain.repository.model.Filtering;
import vn.id.thongdanghoang.sep.prodcat.domain.repository.model.Paging;
import vn.id.thongdanghoang.sep.prodcat.domain.repository.model.Paging.Page;
import vn.id.thongdanghoang.sep.prodcat.domain.repository.model.Sorting;
import vn.id.thongdanghoang.sep.prodcat.entity.prodcat.ProductEntity;

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
