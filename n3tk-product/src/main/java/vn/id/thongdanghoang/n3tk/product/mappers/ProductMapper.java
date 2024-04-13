package vn.id.thongdanghoang.n3tk.product.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import vn.id.thongdanghoang.n3tk.product.dtos.ProductDto;
import vn.id.thongdanghoang.n3tk.product.entities.ProductEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    ProductDto productEntityToProductDto(ProductEntity productEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "transientHashCodeLeaked", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastModificationDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "productReviews", ignore = true)
    @Mapping(target = "productMetas", ignore = true)
    @Mapping(target = "productCategories", ignore = true)
    @Mapping(target = "productTags", ignore = true)
    ProductEntity toEntityForInsert(ProductDto productDto);

    @Mapping(target = "transientHashCodeLeaked", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "lastModificationDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "productReviews", ignore = true)
    @Mapping(target = "productMetas", ignore = true)
    @Mapping(target = "productCategories", ignore = true)
    @Mapping(target = "productTags", ignore = true)
    void toEntityForModify(ProductDto productDto, @MappingTarget ProductEntity productEntity);

}
