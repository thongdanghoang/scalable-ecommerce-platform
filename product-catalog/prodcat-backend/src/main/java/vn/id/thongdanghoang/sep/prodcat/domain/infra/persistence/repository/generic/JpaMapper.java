package vn.id.thongdanghoang.sep.prodcat.domain.infra.persistence.repository.generic;

import vn.id.thongdanghoang.sep.prodcat.domain.repository.model.Filtering;
import vn.id.thongdanghoang.sep.prodcat.domain.repository.model.Filtering.AndFilter;
import vn.id.thongdanghoang.sep.prodcat.domain.repository.model.Filtering.OrFilter;
import vn.id.thongdanghoang.sep.prodcat.domain.repository.model.Filtering.SimpleFilter;
import vn.id.thongdanghoang.sep.prodcat.domain.repository.model.Paging;
import vn.id.thongdanghoang.sep.prodcat.domain.repository.model.Sorting;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import lombok.experimental.UtilityClass;

/**
 * This mapper converts business model filtering, sorting and paging parameters into corresponding
 * JPA Criteria API models.
 */
@UtilityClass
public class JpaMapper {

  private static final String LIST_SEPARATOR = ",";

  /**
   * Converts Filtering to JPA Criteria Predicate
   */
  public <T> Optional<Predicate> toPredicate(
      Filtering filtering,
      Root<T> root,
      CriteriaBuilder cb
  ) {
    return toPredicate(filtering, root, cb, Function.identity());
  }

  /**
   * Converts Filtering to JPA Criteria Predicate with field name adapter
   */
  public <T> Optional<Predicate> toPredicate(
      Filtering filtering,
      Root<T> root,
      CriteriaBuilder cb,
      Function<String, String> adapter
  ) {
    return toPredicate(filtering, root, cb, adapter, DefaultTypeCaster::cast);
  }

  /**
   * Converts Filtering to JPA Criteria Predicate with field name adapter and type caster
   */
  public <T> Optional<Predicate> toPredicate(
      Filtering filtering,
      Root<T> root,
      CriteriaBuilder cb,
      Function<String, String> adapter,
      BiFunction<String, Object, Object> typeCaster
  ) {
    return Optional.ofNullable(filtering)
        .filter(f -> !f.getFilters().isEmpty())
        .map(fs -> fs.getFilters().stream()
            .map(filter -> toPredicate(filter, root, cb, adapter, typeCaster))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toArray(Predicate[]::new))
        .filter(predicates -> predicates.length > 0)
        .map(cb::and);
  }

  private <T> Optional<Predicate> toPredicate(
      Filtering.Filter filter,
      Root<T> root,
      CriteriaBuilder cb,
      Function<String, String> adapter,
      BiFunction<String, Object, Object> typeCaster
  ) {
    if (filter instanceof OrFilter orFilter) {
      Predicate[] orPredicates = orFilter.getSubFilters().stream()
          .map(subFilter -> toPredicate(subFilter, root, cb, adapter, typeCaster))
          .filter(Optional::isPresent)
          .map(Optional::get)
          .toArray(Predicate[]::new);
      return orPredicates.length > 0 ? Optional.of(cb.or(orPredicates)) : Optional.empty();
    } else if (filter instanceof AndFilter andFilter) {
      Predicate[] andPredicates = andFilter.getSubFilters().stream()
          .map(subFilter -> toPredicate(subFilter, root, cb, adapter, typeCaster))
          .filter(Optional::isPresent)
          .map(Optional::get)
          .toArray(Predicate[]::new);
      return andPredicates.length > 0 ? Optional.of(cb.and(andPredicates)) : Optional.empty();
    } else if (filter instanceof SimpleFilter simpleFilter) {
      return toPredicate(simpleFilter, root, cb, adapter, typeCaster);
    }
    return Optional.empty();
  }

  private <T> Optional<Predicate> toPredicate(
      SimpleFilter filter,
      Root<T> root,
      CriteriaBuilder cb,
      Function<String, String> adapter,
      BiFunction<String, Object, Object> typeCaster
  ) {
    String fieldName = Optional.ofNullable(adapter)
        .orElse(Function.identity())
        .apply(filter.name());

    Path<Object> path = getPath(root, fieldName);
    Object value = filter.value();

    return switch (filter.operator()) {
      case NOT_EQUAL -> {
        Object castValue = typeCaster.apply(filter.name(), value);
        yield Optional.of(cb.notEqual(path, castValue));
      }
      case EQUAL -> {
        Object castValue = typeCaster.apply(filter.name(), value);
        yield Optional.of(cb.equal(path, castValue));
      }
      case LIKE -> {
        String likeValue = String.format("%%%s%%", value.toString());
        yield Optional.of(cb.like(cb.lower(path.as(String.class)), likeValue.toLowerCase()));
      }
      case IS_EMPTY -> {
        yield Optional.of(cb.isEmpty(path.as(Collection.class)));
      }
      case IS_NULL -> {
        yield Optional.of(cb.isNull(path));
      }
      case LESS_THAN_OR_EQUAL -> {
        Object castValue = typeCaster.apply(filter.name(), value);
        yield Optional.of(cb.lessThanOrEqualTo(path.as(Comparable.class), (Comparable) castValue));
      }
      case GREATER_THAN_OR_EQUAL -> {
        Object castValue = typeCaster.apply(filter.name(), value);
        yield Optional.of(
            cb.greaterThanOrEqualTo(path.as(Comparable.class), (Comparable) castValue));
      }
      case IN -> {
        List<Object> castValues = toList(typeCaster, filter.name(), value);
        yield Optional.of(path.in(castValues));
      }
    };
  }

  private <T> Path<Object> getPath(Root<T> root, String fieldName) {
    if (fieldName.contains(".")) {
      String[] parts = fieldName.split("\\.");
      Path<Object> path = root.get(parts[0]);
      for (int i = 1; i < parts.length; i++) {
        path = path.get(parts[i]);
      }
      return path;
    }
    return root.get(fieldName);
  }

  private List<Object> toList(BiFunction<String, Object, Object> typeCaster, String name,
      Object value) {
    if (!(value instanceof String)) {
      if (value instanceof List) {
        return (List<Object>) value;
      }
      return List.of(value);
    }
    return Arrays.stream(((String) value).split(LIST_SEPARATOR))
        .map(s -> typeCaster.apply(name, s))
        .toList();
  }

  /**
   * Converts Sorting to JPA Criteria Order list
   */
  public <T> List<Order> toOrders(Sorting sorting, Root<T> root, CriteriaBuilder cb) {
    return toOrders(sorting, root, cb, Function.identity());
  }

  /**
   * Converts Sorting to JPA Criteria Order list with field name adapter
   */
  public <T> List<Order> toOrders(
      Sorting sorting,
      Root<T> root,
      CriteriaBuilder cb,
      Function<String, String> adapter
  ) {
    if (sorting == null) {
      return Collections.emptyList();
    }

    return sorting.toList().stream()
        .map(column -> {
          String fieldName = Optional.ofNullable(adapter)
              .orElse(Function.identity())
              .apply(column.name());
          Path<Object> path = getPath(root, fieldName);

          return switch (column.direction()) {
            case Ascending -> cb.asc(path);
            case Descending -> cb.desc(path);
          };
        })
        .toList();
  }

  /**
   * Converts Paging to offset and limit values
   */
  public PagingInfo toPagingInfo(Paging paging) {
    if (paging == null || paging.isEmpty()) {
      return new PagingInfo(0, Integer.MAX_VALUE);
    }
    int offset = paging.page() * paging.size();
    return new PagingInfo(offset, paging.size());
  }

  /**
   * Record to hold paging information for JPA queries
   */
  public record PagingInfo(int offset, int limit) {

  }
}