package vn.id.thongdanghoang.sep.prodcat.domain.infra.persistence.repository;

import vn.id.thongdanghoang.sep.prodcat.domain.infra.persistence.mapper.prodcat.ProductCategoryMapper;
import vn.id.thongdanghoang.sep.prodcat.domain.infra.persistence.repository.generic.AbstractFullCRUDRepository;
import vn.id.thongdanghoang.sep.prodcat.domain.model.ProductCategory;
import vn.id.thongdanghoang.sep.prodcat.domain.repository.ProductCategoryRepository;
import vn.id.thongdanghoang.sep.prodcat.entity.prodcat.ProductCategoryEntity;
import vn.id.thongdanghoang.sep.prodcat.repositories.ProductCategoryPanacheRepository;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public final class ProductCategoryRepositoryAdapter extends
    AbstractFullCRUDRepository<ProductCategory, ProductCategoryEntity, ProductCategoryMapper>
    implements ProductCategoryRepository {

  private final ProductCategoryPanacheRepository repository;
  private final ProductCategoryMapper mapper;

  @Override
  protected PanacheRepositoryBase<ProductCategoryEntity, UUID> repository() {
    return repository;
  }

  @Override
  protected ProductCategoryMapper mapper() {
    return mapper;
  }

  @Override
  protected Class<ProductCategoryEntity> getEntityClass() {
    return ProductCategoryEntity.class;
  }
}
