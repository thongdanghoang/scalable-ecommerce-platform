package vn.id.thongdanghoang.sep.prodcat.domain.infra.persistence.mapper.prodcat;

import vn.id.thongdanghoang.sep.prodcat.domain.model.Product;
import vn.id.thongdanghoang.sep.prodcat.entity.prodcat.ProductEntity;
import vn.id.thongdanghoang.sep.prodcat.mappers.IMapper;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = JAKARTA_CDI,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductMapper extends IMapper<Product, ProductEntity> {

}
