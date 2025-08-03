package vn.id.thongdanghoang.sep.prodcat.domain.infra.persistence.mapper.prodcat;

import vn.id.thongdanghoang.sep.prodcat.domain.model.ProductCategory;
import vn.id.thongdanghoang.sep.prodcat.entity.prodcat.ProductCategoryEntity;
import vn.id.thongdanghoang.sep.prodcat.mappers.IMapper;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

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