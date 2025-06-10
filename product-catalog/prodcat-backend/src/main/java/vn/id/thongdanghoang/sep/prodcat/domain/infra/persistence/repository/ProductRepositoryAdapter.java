package vn.id.thongdanghoang.sep.prodcat.domain.infra.persistence.repository;

import vn.id.thongdanghoang.sep.prodcat.domain.infra.persistence.mapper.prodcat.ProductMapper;
import vn.id.thongdanghoang.sep.prodcat.domain.infra.persistence.repository.generic.AbstractFullCRUDRepository;
import vn.id.thongdanghoang.sep.prodcat.domain.model.Product;
import vn.id.thongdanghoang.sep.prodcat.domain.repository.ProductRepository;
import vn.id.thongdanghoang.sep.prodcat.entity.prodcat.ProductEntity;
import vn.id.thongdanghoang.sep.prodcat.repositories.ProductPanacheRepository;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public final class ProductRepositoryAdapter extends
    AbstractFullCRUDRepository<Product, ProductEntity, ProductMapper>
    implements ProductRepository {

  private final ProductPanacheRepository repository;
  private final ProductMapper mapper;

  @Override
  protected PanacheRepositoryBase<ProductEntity, UUID> repository() {
    return repository;
  }

  @Override
  protected ProductMapper mapper() {
    return mapper;
  }

  @Override
  protected Class<ProductEntity> getEntityClass() {
    return ProductEntity.class;
  }
}
