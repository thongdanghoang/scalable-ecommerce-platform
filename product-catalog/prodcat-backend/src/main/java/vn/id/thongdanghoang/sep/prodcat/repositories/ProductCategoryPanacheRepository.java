package vn.id.thongdanghoang.sep.prodcat.repositories;


import vn.id.thongdanghoang.sep.prodcat.entity.prodcat.ProductCategoryEntity;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class ProductCategoryPanacheRepository implements
    PanacheRepositoryBase<ProductCategoryEntity, UUID> {

}
