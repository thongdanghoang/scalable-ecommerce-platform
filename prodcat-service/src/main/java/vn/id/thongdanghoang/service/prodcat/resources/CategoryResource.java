package vn.id.thongdanghoang.service.prodcat.resources;

import vn.id.thongdanghoang.service.prodcat.dtos.CategoryDto;
import vn.id.thongdanghoang.service.prodcat.mappers.CategoryMapper;
import vn.id.thongdanghoang.service.prodcat.services.CategoryService;
import vn.id.thongdanghoang.service.prodcat.views.CategoryView;

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

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class CategoryResource {

  private final CategoryService service;
  private final CategoryMapper mapper;

  @POST
  public CategoryView create(@Valid CategoryDto dto) {
    var entity = mapper.toEntity(dto);
    // Manually handle associations (e.g., load parent/children by ID)
    return mapper.toCategoryView(service.create(entity));
  }

  @GET
  @Path("/{id}")
  public CategoryView get(@PathParam("id") UUID id) {
    return mapper.toCategoryView(service.findById(id));
  }
}
