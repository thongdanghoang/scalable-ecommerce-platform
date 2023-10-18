package fptu.swp391.shoppingcart.product.mapping;

import fptu.swp391.shoppingcart.IMapper;
import fptu.swp391.shoppingcart.product.dto.ProductDto;
import fptu.swp391.shoppingcart.product.entity.Image;
import fptu.swp391.shoppingcart.product.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper implements IMapper<Product, ProductDto> {

    @Override
    public Product toEntity(ProductDto productDto) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Product> toEntities(List<ProductDto> productDtos) {
        return productDtos.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public ProductDto toDTO(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setSku(product.getSku());
        productDto.setName(product.getName());
        productDto.setImage(product.getImages().stream().findFirst().orElseThrow().getUrl());
        productDto.setRated(product.getRated());
        productDto.setNumberOfSold(product.getNumberOfSold());
        productDto.setPrice(product.getPrice());
        productDto.setDiscount(product.getDiscount());
        productDto.setCategory(product.getCategory().getFullName());
        return productDto;
    }

    @Override
    public List<ProductDto> toDTOs(List<Product> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }

}
