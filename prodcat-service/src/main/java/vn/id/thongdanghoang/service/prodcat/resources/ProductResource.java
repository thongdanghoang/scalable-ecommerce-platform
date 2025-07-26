package vn.id.thongdanghoang.service.prodcat.resources;

import vn.id.thongdanghoang.service.prodcat.dtos.ProductDto;
import vn.id.thongdanghoang.service.prodcat.mappers.ProductMapper;
import vn.id.thongdanghoang.service.prodcat.services.ProductService;
import vn.id.thongdanghoang.service.prodcat.views.ProductView;

import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class ProductResource {

  private final ProductService service;
  private final ProductMapper mapper;

  @POST
  public ProductView create(@Valid ProductDto dto) {
    var entity = mapper.toEntity(dto);
    // Manually handle associations (e.g., load categories by ID)
    return mapper.toProductView(service.create(entity));
  }

  @GET
  @Path("/{id}")
  public ProductView get(@PathParam("id") UUID id) {
    return mapper.toProductView(service.findById(id));
  }
}
