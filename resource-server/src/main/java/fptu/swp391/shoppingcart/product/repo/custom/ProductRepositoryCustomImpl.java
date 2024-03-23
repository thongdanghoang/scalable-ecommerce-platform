package fptu.swp391.shoppingcart.product.repo.custom;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import fptu.swp391.shoppingcart.product.entity.Product;
import fptu.swp391.shoppingcart.product.entity.QCategory;
import fptu.swp391.shoppingcart.product.entity.QProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    private static void sorting(String sort, JPAQuery<Product> query, QProduct product) {
        if (sort != null && !sort.isEmpty()) {
            String[] sortingFactors = sort.split(",");
            for (String sortingFactor : sortingFactors) {
                String[] parts = sortingFactor.split(":");
                if (parts.length == 2) {
                    String field = parts[0];
                    String direction = parts[1];

                    if (field.equals("popular")) {
                        if (direction.equals("asc")) {
                            query.orderBy(product.numberOfSold.asc());
                        } else if (direction.equals("desc")) {
                            query.orderBy(product.numberOfSold.desc());
                        }
                    }

                    if (field.equals("price")) {
                        if (direction.equals("asc")) {
                            query.orderBy(product.price.asc());
                        } else if (direction.equals("desc")) {
                            query.orderBy(product.price.desc());
                        }
                    }

                    if (field.equals("name")) {
                        if (direction.equals("asc")) {
                            query.orderBy(product.name.asc());
                        } else if (direction.equals("desc")) {
                            query.orderBy(product.name.desc());
                        }
                    }

                    if (field.equals("rated")) {
                        if (direction.equals("asc")) {
                            query.orderBy(product.rated.asc());
                        } else if (direction.equals("desc")) {
                            query.orderBy(product.rated.desc());
                        }
                    }
                }
            }
        }
    }

    @Override
    public Page<Product> search(String keyword, String sort, String category, String size, String colour,
                                int minPrice, int maxPrice,
                                Pageable pageable) {
        QProduct product = QProduct.product;
        BooleanExpression conditions = null;
        if (keyword != null && !keyword.isEmpty()) {
            conditions = product.name.containsIgnoreCase(keyword);
        }

        if (size != null && !size.isEmpty()) {
            for (String s : size.split(",")) {
                conditions = conditions == null ? product.quantities.any().size.sizeName.containsIgnoreCase(s) :
                        conditions.and(product.quantities.any().size.sizeName.containsIgnoreCase(s));
            }
        }

        if (colour != null && !colour.isEmpty()) {
            for (String c : colour.split(",")) {
                conditions = conditions == null ? product.quantities.any().color.colorName.containsIgnoreCase(c) :
                        conditions.and(product.quantities.any().color.colorName.containsIgnoreCase(c));
            }
        }

        if (minPrice > 0) {
            conditions = conditions == null ? product.price.goe(minPrice) :
                    conditions.and(product.price.goe(minPrice));
        }
        if (maxPrice > 0) {
            conditions = conditions == null ? product.price.loe(maxPrice) :
                    conditions.and(product.price.loe(maxPrice));
        }

        if (category != null && !category.isEmpty()) {
            conditions = conditions == null ? getCategoryExpression(product.category, category) :
                    conditions.and(getCategoryExpression(product.category, category));
        }

        JPAQuery<Product> query = new JPAQuery<Product>(em)
                .from(product)
                .where(conditions);

        // Handle sorting
        sorting(sort, query, product);

        long totalCount = query.fetchCount();
        List<Product> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return PageableExecutionUtils.getPage(results, pageable, () -> totalCount);
    }

    private BooleanExpression getCategoryExpression(QCategory category, String inputKeyword) {
        String[] keywords = inputKeyword.split(" ");
        BooleanExpression resultExpression = null;

        // Xử lý mỗi từ khóa và kết hợp chúng vào biểu thức truy vấn
        for (String keyword : keywords) {
            BooleanExpression currentExpression = getCategoryRecursiveExpression(category, keyword);
            resultExpression = (resultExpression == null) ? currentExpression : resultExpression.and(currentExpression);
        }

        return resultExpression;
    }

    private BooleanExpression getCategoryRecursiveExpression(QCategory category, String categoryName) {
        if (category == null) {
            return null;
        }

        BooleanExpression currentExpression = category.name.containsIgnoreCase(categoryName);

        // Thực hiện đệ quy cho parentCategory
        BooleanExpression parentExpression = getCategoryRecursiveExpression(category.parentCategory, categoryName);

        // Kết hợp biểu thức hiện tại với biểu thức đệ quy của parent
        return currentExpression.or(parentExpression);
    }
}
