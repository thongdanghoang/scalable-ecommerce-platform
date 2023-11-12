package fptu.swp391.shoppingcart.product.mapping;

import fptu.swp391.shoppingcart.IMapper;
import fptu.swp391.shoppingcart.product.dto.ClassifyClotheDto;
import fptu.swp391.shoppingcart.product.dto.ProductDetailDto;
import fptu.swp391.shoppingcart.product.dto.QuantityBySizeDto;
import fptu.swp391.shoppingcart.product.entity.Image;
import fptu.swp391.shoppingcart.product.entity.Product;
import fptu.swp391.shoppingcart.product.entity.Quantity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProductDetailMapper implements IMapper<Product, ProductDetailDto> {

    private static List<QuantityBySizeDto> mapQuantityToProductQuantityDto(Long colorId, List<Quantity> quantities) {
        List<QuantityBySizeDto> quantitiesBySize = new ArrayList<>();
        for (Quantity quantity : quantities) {
            if (Objects.equals(quantity.getColor().getId(), colorId)) {
                QuantityBySizeDto quantityBySizeDto = new QuantityBySizeDto();
                quantityBySizeDto.setSize(quantity.getSize().getSizeName());
                quantityBySizeDto.setQuantityInStock(quantity.getQuantityInStock());
                quantityBySizeDto.setQuantityId(quantity.getId());
                quantitiesBySize.add(quantityBySizeDto);
            }
        }
        return quantitiesBySize;
    }

    @Override
    public Product toEntity(ProductDetailDto productDetailDto) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Product> toEntities(List<ProductDetailDto> productDetailDTOs) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public ProductDetailDto toDTO(Product product) {
        ProductDetailDto productDto = new ProductDetailDto();
        List<ClassifyClotheDto> classifyClotheDTOs = new ArrayList<>();
        List<Image> images = product.getImages();
        List<Quantity> quantities = product.getQuantities();

        // group images by images.color
        Map<Long, String> colours = new HashMap<>();
        if (!images.isEmpty()) {
            for (Image image : images) {
                if (image.getColor() != null
                        && (!colours.containsKey(image.getColor().getId()))) {
                    colours.put(image.getColor().getId(), image.getColor().getColorName());
                }
            }
            colours.forEach((colorId, colorName) -> {
                ClassifyClotheDto classifyClotheDto = new ClassifyClotheDto();

                classifyClotheDto.setColor(colorName);

                List<String> imageUrls = images.stream()
                        .filter(image -> Objects.equals(image.getColor().getId(), colorId))
                        .map(Image::getUrl)
                        .collect(Collectors.toList());
                classifyClotheDto.setImages(imageUrls);

                List<QuantityBySizeDto> quantitiesBySize = mapQuantityToProductQuantityDto(colorId, quantities);
                classifyClotheDto.setQuantities(quantitiesBySize);
                classifyClotheDTOs.add(classifyClotheDto);
            });
        }

        productDto.setId(product.getId());
        productDto.setSku(product.getSku());
        productDto.setName(product.getName());
        productDto.setRated(product.getRated());
        productDto.setNumberOfSold(product.getNumberOfSold());
        productDto.setPrice(product.getPrice());
        productDto.setDiscount(product.getDiscount());
        productDto.setCategory(product.getCategory().getFullName());
        productDto.setDescription(product.getDescription());
        productDto.setClassifyClothes(classifyClotheDTOs);
        return productDto;
    }

    @Override
    public List<ProductDetailDto> toDTOs(List<Product> entities) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
