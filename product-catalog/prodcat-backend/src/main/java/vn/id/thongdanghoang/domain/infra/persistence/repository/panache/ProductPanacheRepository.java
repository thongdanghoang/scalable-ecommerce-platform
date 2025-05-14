package vn.id.thongdanghoang.domain.infra.persistence.repository.panache;


import vn.id.thongdanghoang.domain.infra.persistence.entity.prodcat.ProductEntity;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class ProductPanacheRepository implements PanacheRepositoryBase<ProductEntity, UUID> {

}
