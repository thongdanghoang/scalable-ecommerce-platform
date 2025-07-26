package vn.id.thongdanghoang.service.prodcat.services;

import vn.id.thongdanghoang.service.prodcat.entities.Category;
import vn.id.thongdanghoang.service.prodcat.repositories.CategoryRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Transactional(rollbackOn = Throwable.class)
public class CategoryService {

  private final CategoryRepository repository;

  public Category create(Category category) {
    repository.persist(category);
    return category;
  }

  public Category update(UUID id, Category updated) {
    var existing = repository.findByIdOptional(id).orElseThrow();
    existing.setName(updated.getName());
    existing.setDescription(updated.getDescription());
    // Handle associations as needed
    return existing;
  }

  public Category findById(UUID id) {
    return repository.findById(id);
  }

}
