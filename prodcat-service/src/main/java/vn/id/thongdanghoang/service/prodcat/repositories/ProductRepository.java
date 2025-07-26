package vn.id.thongdanghoang.service.prodcat.repositories;

import vn.id.thongdanghoang.service.prodcat.entities.Product;

import jakarta.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class ProductRepository  extends AbstractRepository<Product> {

  public List<Product> findByPriceRange(BigDecimal min, BigDecimal max) {
    var cb = getEntityManager().getCriteriaBuilder();
    var query = cb.createQuery(Product.class);
    var root = query.from(Product.class);

    query.where(cb.between(root.get("price"), min, max));

    return getEntityManager().createQuery(query).getResultList();
  }

}
