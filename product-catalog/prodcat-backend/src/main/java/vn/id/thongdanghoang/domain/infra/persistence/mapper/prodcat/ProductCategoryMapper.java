package vn.id.thongdanghoang.domain.infra.persistence.mapper.prodcat;

import vn.id.thongdanghoang.domain.model.ProductCategory;
import vn.id.thongdanghoang.domain.infra.persistence.entity.prodcat.ProductCategoryEntity;
import vn.id.thongdanghoang.domain.infra.persistence.mapper.IMapper;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;

@Mapper(
    componentModel = JAKARTA_CDI,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public abstract class ProductCategoryMapper implements
    IMapper<ProductCategory, ProductCategoryEntity> {

  @Override
  public void partialUpdate(ProductCategoryEntity source, ProductCategoryEntity target) {
    if (StringUtils.isNotBlank(source.getName())) {
      target.setName(source.getName());
    }
    if (Objects.nonNull(source.getParent())) {
      target.setParent(source.getParent());
    }
    if (StringUtils.isNotBlank(source.getDescription())) {
      target.setDescription(source.getDescription());
    }
  }

  @Override
  public void fullUpdate(ProductCategoryEntity source, ProductCategoryEntity target) {
    target.setName(source.getName());
    target.setParent(source.getParent());
    target.setDescription(source.getDescription());
  }
}