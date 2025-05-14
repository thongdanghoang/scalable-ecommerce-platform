package vn.id.thongdanghoang.api.rest.mapper;

import vn.id.thongdanghoang.api.rest.dto.ProductDto;
import vn.id.thongdanghoang.domain.model.*;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

import java.util.*;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;

@Mapper(componentModel = JAKARTA_CDI)
public abstract class ProductMapper {

  public abstract Product toModel(ProductDto source);

  public abstract ProductDto toDto(Product source);

  Set<ProductCategory> toCategories(Set<UUID> categories) {
    return categories.stream()
        .map(ProductCategory::new)
        .collect(Collectors.toUnmodifiableSet());
  }

  Set<UUID> toCategoryIDs(Set<ProductCategory> categories) {
    return categories.stream()
        .map(ProductCategory::getId)
        .collect(Collectors.toUnmodifiableSet());
  }
}
