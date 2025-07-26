package vn.id.thongdanghoang.service.prodcat.services;

import vn.id.thongdanghoang.service.prodcat.entities.Product;
import vn.id.thongdanghoang.service.prodcat.repositories.ProductRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
@Transactional(rollbackOn = Throwable.class)
public class ProductService {

  private final ProductRepository repository;

  public Product create(Product product) {
    repository.persist(product);
    return product;
  }

  public Product update(UUID id, Product updated) {
    var existing = repository.findByIdOptional(id).orElseThrow();
    existing.setName(updated.getName());
    existing.setDescription(updated.getDescription());
    existing.setPrice(updated.getPrice());
    // Handle associations as needed
    return existing;
  }

  public Product findById(UUID id) {
    return repository.findById(id);
  }
}
