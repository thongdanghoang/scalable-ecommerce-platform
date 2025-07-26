package vn.id.thongdanghoang.service.prodcat.repositories;

import vn.id.thongdanghoang.entities.BaseEntity;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.criteria.CriteriaBuilder;
import java.util.UUID;

public abstract class AbstractRepository<T extends BaseEntity>
    implements PanacheRepositoryBase<T, UUID> {
  public static final String ENTITY_GRAPH_HINT_PARAM_NAME = "jakarta.persistence.fetchgraph";

  /**
   * Return a named EntityGraph. The returned EntityGraph
   * should be considered immutable.
   *
   * @param graphName name of an existing entity graph
   * @return named entity graph
   */
  public EntityGraph<?> getEntityGraph(String graphName) {
    return getEntityManager().getEntityGraph(graphName);
  }

  protected PanacheQuery<T> findWithGraph(String query, Parameters parameters, EntityGraph<?> entityGraph) {
    return find(query, parameters).withHint(ENTITY_GRAPH_HINT_PARAM_NAME, entityGraph);
  }

  protected CriteriaBuilder getCriteriaBuilder() {
    return getEntityManager().getCriteriaBuilder();
  }

}
