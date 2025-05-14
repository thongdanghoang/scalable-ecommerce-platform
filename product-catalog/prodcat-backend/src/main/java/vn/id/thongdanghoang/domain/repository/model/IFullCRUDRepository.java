package vn.id.thongdanghoang.domain.repository.model;

import io.smallrye.mutiny.Uni;
import java.util.UUID;

public interface IFullCRUDRepository<MODEL> extends IReadOnlyRepository<MODEL> {

  Uni<MODEL> create(MODEL model);

  Uni<Void> update(UUID primaryKey, MODEL model, boolean partialUpdate);

  Uni<Void> delete(UUID primaryKey);
}
