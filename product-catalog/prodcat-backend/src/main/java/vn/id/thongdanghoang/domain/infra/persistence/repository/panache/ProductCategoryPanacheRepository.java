package vn.id.thongdanghoang.domain.infra.persistence.repository.panache;


import vn.id.thongdanghoang.domain.infra.persistence.entity.prodcat.*;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class ProductCategoryPanacheRepository implements
    PanacheRepositoryBase<ProductCategoryEntity, UUID> {

}
