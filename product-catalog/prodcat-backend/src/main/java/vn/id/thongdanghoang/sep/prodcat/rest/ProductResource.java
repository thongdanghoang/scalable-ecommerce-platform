package vn.id.thongdanghoang.sep.prodcat.rest;

import vn.id.thongdanghoang.sep.prodcat.domain.logic.ProductService;
import vn.id.thongdanghoang.sep.prodcat.domain.model.ProductCategory;
import vn.id.thongdanghoang.sep.prodcat.domain.repository.model.Filtering;
import vn.id.thongdanghoang.sep.prodcat.domain.repository.model.Paging;
import vn.id.thongdanghoang.sep.prodcat.domain.repository.model.Sorting;
import vn.id.thongdanghoang.sep.prodcat.dto.ProductDto;
import vn.id.thongdanghoang.sep.prodcat.mappers.ProductMapper;

import io.smallrye.mutiny.Uni;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

@Path(ProductResource.PATH)
@RequiredArgsConstructor
public class ProductResource {

  public static final String PATH = "products";

  private final ProductService service;
  private final ProductMapper mapper;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<Response> search(
      @QueryParam("page") @DefaultValue("0") @PositiveOrZero int page,
      @QueryParam("size") @DefaultValue("10") @PositiveOrZero @Max(100) int size) {
    return service.search(Filtering.empty(), Sorting.empty(), Paging.of(page, size))
        .map(result -> Response.ok(result).build());
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<Response> getProduct(@PathParam("id") UUID id) {
    return service.getProduct(id)
        .map(product -> Response.ok(product).build());
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<Response> createProduct() {
    // First, fetch available categories from the database
    return service.searchProductCategories(Filtering.empty(), Sorting.empty(), Paging.of(0, 20))
        .map(categoryPage -> {
          var availableCategories = categoryPage.items();
          Set<ProductCategory> selectedCategories = new HashSet<>();

          // Pick 1-3 random categories if any exist
          if (!availableCategories.isEmpty()) {
            int numCategoriesToPick = Math.min(
                availableCategories.size(),
                1 + (int) (Math.random() * 3) // Random number between 1-3
            );

            // Convert to list for easier random access
            var categoryList = new ArrayList<>(availableCategories);

            // Pick random categories
            for (int i = 0; i < numCategoriesToPick; i++) {
              int randomIndex = (int) (Math.random() * categoryList.size());
              selectedCategories.add(categoryList.remove(randomIndex));
            }
          }

          return selectedCategories;
        })
        .flatMap(selectedCategories -> {
          var productDto = new ProductDto(
              null, // id
              null, // version
              RandomStringUtils.secure().nextAlphabetic(8), // sku
              null, // name
              null, // description
              null, // price
              null, // discount
              false, // active
              false, // shippable
              null, // variant
              selectedCategories.stream()
                  .map(ProductCategory::getId)
                  .collect(Collectors.toSet()), // categories
              Collections.emptySet() // images
          );

          return service.createProduct(mapper.toModel(productDto))
              .map(productId -> Response
                  .created(UriBuilder
                      .fromResource(ProductResource.class).path("/{id}")
                      .build(productId)
                  )
                  .build());
        });
  }

  @GET
  @Path("category")
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<Response> searchCategories(
      @QueryParam("page") @DefaultValue("0") @PositiveOrZero int page,
      @QueryParam("size") @DefaultValue("10") @PositiveOrZero @Max(100) int size) {
    return service.searchProductCategories(Filtering.empty(), Sorting.empty(),
            Paging.of(page, size))
        .map(result -> Response.ok(result).build());
  }

  @POST
  @Path("category")
  @Produces(MediaType.APPLICATION_JSON)
  public Uni<Response> createCategory() {
    return service.searchProductCategories(Filtering.empty(), Sorting.empty(), Paging.empty())
        .map(page -> page.items().stream().findAny())
        .map(parent -> {
          var category = new ProductCategory();
          category.setName(RandomStringUtils.secure().nextAlphabetic(8));
          category.setDescription(RandomStringUtils.secure().nextAlphabetic(64));
          if (Integer.parseInt(RandomStringUtils.secure().nextNumeric(2)) % 2 == 0) {
            parent.ifPresent(category::setParent);
          }
          return category;
        })
        .flatMap(service::createProductCategory)
        .map(productCategory -> Response
            .created(UriBuilder
                .fromResource(ProductResource.class).path("/{id}")
                .build(productCategory.getId())
            )
            .build());
  }
}
