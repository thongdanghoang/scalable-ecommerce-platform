package vn.id.thongdanghoang.domain.infra.persistence.repository.generic;

import vn.id.thongdanghoang.domain.infra.persistence.mapper.IMapper;
import vn.id.thongdanghoang.domain.repository.model.*;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.util.*;
import org.hibernate.reactive.mutiny.Mutiny;

/**
 * This class provides generic CRUD features to a &lt;Model, Entity, Mapper&gt; triplet that respect
 * the following conditions.
 *
 * @param <MODEL>  Business data model to be manipulated by the repository. Simple bean with
 *                 getters.
 * @param <ENTITY> Persistence entity mapped to the corresponding DB table. Simple bean with
 *                 getters, annotated with @Entity and columns properly mapped.
 * @param <MAPPER> Mapstruct interface that converts model to entity and vice versa. Extends IMapper
 *                 interface.
 */
public abstract class AbstractFullCRUDRepository
    <MODEL, ENTITY, MAPPER extends IMapper<MODEL, ENTITY>>
    implements IFullCRUDRepository<MODEL> {

  @Inject
  Mutiny.SessionFactory sf;

  protected abstract PanacheRepositoryBase<ENTITY, UUID> repository();

  protected abstract MAPPER mapper();

  protected abstract Class<ENTITY> getEntityClass();

  /**
   * This method allows more natural mapping of field names used in queries and sort.
   *
   * @param fieldName name of the field to query or sort on
   * @return String representing the entity attribute query or filter will be applied on
   */
  protected String adapter(String fieldName) {
    return fieldName;
  }

  /**
   * This method allows customizing the type cast of values in queries. This enables complex search
   * from frontend on boolean, dates, and even enum values.
   *
   * @param fieldName name of the field to query
   * @param value     value to query
   * @return Cast object to be used in the query
   */
  protected Object typeCaster(String fieldName, Object value) {
    return DefaultTypeCaster.cast(fieldName, value);
  }

  @Override
  public Uni<MODEL> create(MODEL model) {
    ENTITY entity = mapper().toEntity(model);
    return repository().persist(entity).map(mapper()::toModel);
  }

  @Override
  public Uni<MODEL> get(UUID primaryKey) {
    return repository().findById(primaryKey)
        .map(mapper()::toModel)
        .onItem().ifNull().failWith(() ->
            new EntityNotFoundException(String.format("Entity not found {%s}.", primaryKey))
        );
  }

  @Override
  public Uni<MODEL> getWithGraph(UUID primaryKey, String graphName) {
    return sf
        .withSession(session -> session
            .find(session.getEntityGraph(getEntityClass(), graphName), primaryKey)
        )
        .map(mapper()::toModel);
  }

  @Override
  public Uni<MODEL> findOne(@Valid Filtering filtering) {
    return sf.withSession(session -> {
      var cb = sf.getCriteriaBuilder();
      var cq = cb.createQuery(getEntityClass());
      var root = cq.from(getEntityClass());

      var predicate = JpaMapper
          .toPredicate(filtering, root, cb, this::adapter, this::typeCaster);
      predicate.ifPresent(cq::where);

      return session.createQuery(cq)
          .setMaxResults(1)
          .getResultList()
          .map(entities -> entities.stream()
              .findFirst()
              .map(mapper()::toModel)
              .orElse(null));
    });
  }

  @Override
  public Uni<Paging.Page<MODEL>> findAll(
      @Valid Filtering filtering, @Valid Sorting sorting, @Valid Paging paging) {
    return findWithOptionalGraph(filtering, sorting, paging, Optional.empty());
  }

  @Override
  public Uni<Paging.Page<MODEL>> findWithGraph(Filtering filtering,
      Sorting sorting,
      Paging paging,
      String graphName) {
    return findWithOptionalGraph(filtering, sorting, paging, Optional.of(graphName));
  }

  private Uni<Paging.Page<MODEL>> findWithOptionalGraph(
      Filtering filtering, Sorting sorting, Paging paging, Optional<String> graphName) {
    var session = sf.getCurrentSession();

    var criteriaBuilder = sf.getCriteriaBuilder();
    var criteriaQuery = criteriaBuilder.createQuery(getEntityClass());
    var root = criteriaQuery.from(getEntityClass());

    var filters = JpaMapper
        .toPredicate(filtering, root, criteriaBuilder, this::adapter, this::typeCaster);
    filters.ifPresent(criteriaQuery::where);

    var orders = JpaMapper.toOrders(sorting, root, criteriaBuilder, this::adapter);
    if (!orders.isEmpty()) {
      criteriaQuery.orderBy(orders);
    }

    var selectionQuery = session.createQuery(criteriaQuery);

    graphName.ifPresent(s -> selectionQuery.setPlan(session.getEntityGraph(getEntityClass(), s)));

    if (!paging.isEmpty()) {
      selectionQuery.setPage(org.hibernate.query.Page.page(paging.size(), paging.page()));
    }

    return Uni.combine().all().unis(selectionQuery.getResultCount(), selectionQuery.getResultList())
        .asTuple()
        .map(tuple -> {
          var totalCount = tuple.getItem1();
          var entities = tuple.getItem2();
          var items = entities.stream().map(mapper()::toModel).toList();
          int pages = totalCount == 0
              ? 1
              : ((int) Math.ceil((double) totalCount / (double) paging.size()));
          return new Paging.Page<>(paging, pages, totalCount, items);
        });
  }

  @Override
  public Uni<Long> count() {
    return repository().count();
  }

  @Override
  public Uni<Void> update(UUID primaryKey, MODEL model, boolean partialUpdate) {
    ENTITY entity = mapper().toEntity(model);
    /* Persist is not required when updating an already persisted entity
     * https://quarkus.io/guides/hibernate-orm-panache#defining-your-entity
     */
    return repository().findById(primaryKey)
        .flatMap(existingEntity -> {

          if (existingEntity == null) {
            return Uni.createFrom().failure(
                new EntityNotFoundException(String.format("Entity not found {%s}.", primaryKey))
            );
          }

          if (partialUpdate) {
            mapper().partialUpdate(entity, existingEntity);
          } else {
            mapper().fullUpdate(entity, existingEntity);
          }

          /* flush is required to trigger @PreUpdate predicate */
          return repository().flush();
        });
  }

  @Override
  public Uni<Void> delete(UUID primaryKey) {
    return repository().deleteById(primaryKey)
        .flatMap(deleted -> {
          if (!deleted) {
            return Uni.createFrom().failure(
                new EntityNotFoundException(String.format("Entity not found {%s}.", primaryKey))
            );
          }
          return Uni.createFrom().voidItem();
        });
  }

  @Override
  public Uni<Boolean> exists(UUID primaryKey) {
    return repository().findById(primaryKey)
        .map(entity -> true)
        .onItem().ifNull().continueWith(false);
  }
}
