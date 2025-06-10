package vn.id.thongdanghoang.sep.prodcat.domain.repository.model;

import io.smallrye.mutiny.Uni;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public interface IReadOnlyRepository<MODEL> {

  Uni<MODEL> get(UUID primaryKey);

  Uni<MODEL> getWithGraph(UUID primaryKey, @NotBlank String graphName);

  Uni<MODEL> findOne(@Valid Filtering filtering);

  Uni<Paging.Page<MODEL>> findAll(
      @Valid Filtering filtering,
      @Valid Sorting sorting,
      @Valid Paging paging
  );

  Uni<Paging.Page<MODEL>> findWithGraph(
      @Valid Filtering filtering,
      @Valid Sorting sorting,
      @Valid Paging paging,
      @NotBlank String graphName
  );

  Uni<Long> count();

  Uni<Boolean> exists(UUID primaryKey);
}
