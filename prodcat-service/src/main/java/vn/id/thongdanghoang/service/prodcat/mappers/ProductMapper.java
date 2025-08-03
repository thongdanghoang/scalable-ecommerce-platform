package vn.id.thongdanghoang.service.prodcat.mappers;

import vn.id.thongdanghoang.service.prodcat.dtos.ProductDto;
import vn.id.thongdanghoang.service.prodcat.entities.Product;
import vn.id.thongdanghoang.service.prodcat.views.ProductView;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = ComponentModel.JAKARTA_CDI,
    uses = {CategoryMapper.class}
)
public interface ProductMapper {

  Product toEntity(ProductDto productDto);

  ProductDto toDto(Product product);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Product partialUpdate(ProductDto productDto, @MappingTarget Product product);

  ProductView toProductView(Product product);
}