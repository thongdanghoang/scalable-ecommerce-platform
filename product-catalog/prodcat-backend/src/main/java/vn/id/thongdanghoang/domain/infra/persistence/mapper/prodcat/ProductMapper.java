package vn.id.thongdanghoang.domain.infra.persistence.mapper.prodcat;

import vn.id.thongdanghoang.domain.model.Product;
import vn.id.thongdanghoang.domain.infra.persistence.entity.prodcat.ProductEntity;
import vn.id.thongdanghoang.domain.infra.persistence.mapper.IMapper;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

import org.mapstruct.*;

@Mapper(
    componentModel = JAKARTA_CDI,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductMapper extends IMapper<Product, ProductEntity> {

}
