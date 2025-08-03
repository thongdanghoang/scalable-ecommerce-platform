package vn.id.thongdanghoang.service.prodcat.mappers;

import vn.id.thongdanghoang.service.prodcat.dtos.CategoryDto;
import vn.id.thongdanghoang.service.prodcat.entities.Category;
import vn.id.thongdanghoang.service.prodcat.views.CategoryView;

import java.util.UUID;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = ComponentModel.JAKARTA_CDI
)
public interface CategoryMapper {

  @Mapping(target = "children", source = "childIds")
  @Mapping(target = "parent", source = "parentId")
  Category toEntity(CategoryDto categoryDto);

  @AfterMapping
  default void linkChildren(@MappingTarget Category category) {
    category.getChildren().forEach(child -> child.setParent(category));
  }

  @InheritInverseConfiguration(name = "toEntity")
  CategoryDto toDto(Category category);

  @InheritConfiguration(name = "toEntity")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Category partialUpdate(CategoryDto categoryDto, @MappingTarget Category category);

  Category toEntity(UUID id);

  UUID toUuid(Category category);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Category partialUpdate(UUID id, @MappingTarget Category category);

  @Mapping(target = "childIds", source = "children")
  @Mapping(target = "parentId", source = "parent")
  CategoryView toCategoryView(Category category);
}