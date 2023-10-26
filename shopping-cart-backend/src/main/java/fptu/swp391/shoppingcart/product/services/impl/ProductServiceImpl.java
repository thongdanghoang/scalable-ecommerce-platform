package fptu.swp391.shoppingcart.product.services.impl;

import fptu.swp391.shoppingcart.product.dto.*;
import fptu.swp391.shoppingcart.product.entity.*;
import fptu.swp391.shoppingcart.product.exceptions.ProductImageNotFoundException;
import fptu.swp391.shoppingcart.product.mapping.ProductDetailMapper;
import fptu.swp391.shoppingcart.product.mapping.ProductMapper;
import fptu.swp391.shoppingcart.product.repo.*;
import fptu.swp391.shoppingcart.product.services.ImageService;
import fptu.swp391.shoppingcart.product.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private QuantityRepository quantityRepository;

    @Autowired
    private ProductMapper mapper;

    @Autowired
    private ProductDetailMapper detailMapper;

    @Autowired
    private ImageService imageService;

    @Override
    public Page<ProductDto> getAll(int page, int limit) {
        return productRepository.findAll(PageRequest.of(page, limit)).map(mapper::toDTO);
    }

    @Override
    public Page<ProductDto> searchByKeyword(int page, int limit, String keyword) {
        return null;
    }

    @Override
    public ProductDetailDto findProductById(Long id) {
        return detailMapper.toDTO(productRepository.findById(id).orElseThrow());
    }

    @Override
    public Page<ProductDto> search(String keyword, String sort, String category,
                                   String size, String colour,
                                   int minPrice, int maxPrice,
                                   int page, int limit) {
        // sort: popular, lowest price, highest price, asc, desc
        //sort=field1:asc,field2:desc,field3:asc
        Pageable pageable = PageRequest.of(page, limit);
        Page<Product> results = productRepository.search(keyword, sort, category, size, colour, minPrice, maxPrice, pageable);
        return results.map(mapper::toDTO);
    }

    @Override
    @Transactional(rollbackFor = {ProductImageNotFoundException.class, Exception.class})
    public ProductAddingDto createProduct(ProductAddingDto productDto) {
        Product product = new Product();// number of sold is 0 and rating is 0
        product.setSku(productDto.getSku());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDiscount(productDto.getDiscount());
        product.setCategory(categoryRepository.findById(productDto.getCategoryId()).orElseThrow());
        List<ClassifyClotheDto> classifyClothes = productDto.getClassifyClothes();
        classifyClothes.stream() // check if color is existed -> if not -> create and save
                .filter(classifyAddingClotheDto -> colorRepository.findByColorName(classifyAddingClotheDto.getColor()).isEmpty())
                .forEach(classifyAddingClotheDto -> colorRepository.save(new Color(classifyAddingClotheDto.getColor())));
        // check if size is existed -> if not -> create and save
        classifyClothes.forEach(classify -> classify.getQuantities()
                .stream()
                .filter(quantityBySizeDto -> sizeRepository.findBySizeName(quantityBySizeDto.getSize()).isEmpty())
                .forEach(quantityBySizeDto -> sizeRepository.save(new Size(quantityBySizeDto.getSize()))));


        List<Quantity> quantities = new ArrayList<>();
        // add quantity
        classifyClothes.forEach(classify -> {
            Color color = colorRepository.findByColorName(classify.getColor()).orElseThrow();
            classify.getQuantities().forEach(quantity -> {
                Quantity quantityEntity = new Quantity();
                quantityEntity.setProduct(product);
                quantityEntity.setColor(color);
                quantityEntity.setSize(sizeRepository.findBySizeName(quantity.getSize()).orElseThrow());
                quantityEntity.setQuantityInStock(quantity.getQuantity());
                quantities.add(quantityEntity);
            });
        });
        product.setQuantities(quantities);
        Product saved = productRepository.save(product);
        quantityRepository.saveAll(saved.getQuantities());

        classifyClothes.forEach(classifyAddingClotheDto -> { // save product's images
            Color color = colorRepository.findByColorName(classifyAddingClotheDto.getColor()).orElseThrow();
            classifyAddingClotheDto.getImages().forEach(img -> { // each color has many images
                Image image = new Image();
                image.setProduct(product);
                image.setColor(color);
                image.setUrl(img);
                if (!imageService.isImageExist(img)) {
                    throw new ProductImageNotFoundException(String.format("Image %s not found in the system.", img));
                }
                imageRepository.save(image);
            });
        });
        return productDto;
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        return categoryRepository.findAll().stream().map(entity -> {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(entity.getId());
            categoryDto.setName(entity.getFullName());
            return categoryDto;
        }).collect(Collectors.toList());
    }
}
