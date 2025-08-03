package vn.id.thongdanghoang.service.prodcat.repositories;

import vn.id.thongdanghoang.service.prodcat.entities.Category;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CategoryRepository extends AbstractRepository<Category> {

  public List<Category> findByNameStartingWith(String prefix) {
    var cb = getCriteriaBuilder();
    var query = cb.createQuery(Category.class);
    var root = query.from(Category.class);

    query.where(cb.like(root.get("name"), prefix + "%"));

    return getEntityManager().createQuery(query).getResultList();
  }

}
