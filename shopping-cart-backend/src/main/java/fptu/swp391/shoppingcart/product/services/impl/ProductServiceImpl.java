package fptu.swp391.shoppingcart.product.services.impl;

import fptu.swp391.shoppingcart.product.dto.ProductDetailDto;
import fptu.swp391.shoppingcart.product.dto.ProductDto;
import fptu.swp391.shoppingcart.product.entity.Product;
import fptu.swp391.shoppingcart.product.mapping.ProductDetailMapper;
import fptu.swp391.shoppingcart.product.mapping.ProductMapper;
import fptu.swp391.shoppingcart.product.repo.ProductRepository;
import fptu.swp391.shoppingcart.product.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper mapper;

    @Autowired
    private ProductDetailMapper detailMapper;

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
}
