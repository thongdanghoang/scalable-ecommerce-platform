package vn.id.thongdanghoang.domain.infra.persistence.repository.generic;

import vn.id.thongdanghoang.domain.repository.model.*;
import vn.id.thongdanghoang.domain.repository.model.Filtering.*;

import io.quarkus.panache.common.*;
import io.quarkus.panache.common.Sort.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.*;
import java.util.stream.*;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * This mapper converts business model filtering, sorting and paging parameters into corresponding
 * panache specific models.
 */
@UtilityClass
public class PanacheMapper {

  private static final String LIST_SEPARATOR = ",";

  public Optional<Entry<String, Parameters>> toQuery(Filtering filtering) {
    return toQuery(filtering, Function.identity());
  }

  public Optional<Entry<String, Parameters>> toQuery(
      Filtering filtering,
      Function<String, String> adapter
  ) {
    return toQuery(filtering, adapter, DefaultTypeCaster::cast);
  }

  public Optional<Entry<String, Parameters>> toQuery(
      Filtering filtering,
      Function<String, String> adapter,
      BiFunction<String, Object, Object> typeCaster
  ) {
    Parameters parameters = new Parameters();
    return Optional.ofNullable(filtering)
        .filter(f -> !f.getFilters().isEmpty())
        .map(fs -> IntStream.range(0, fs.getFilters().size())
            .boxed()
            .map(i -> toQuery(i.toString(), fs.getFilters().get(i), adapter, typeCaster))
            .map(e -> {
              e.getValue().map().forEach(parameters::and);
              return e.getKey();
            })
            .map(q -> String.format("( %s )", q))
            .collect(Collectors.joining(" AND "))
        )
        .map(q -> new SimpleEntry<>(q, parameters));
  }

  private Entry<String, Parameters> toQuery(
      String index,
      Filtering.Filter filter,
      Function<String, String> adapter,
      BiFunction<String, Object, Object> typeCaster
  ) {
    Parameters parameters = new Parameters();
    if (filter instanceof OrFilter orFilter) {
      String orQuery = IntStream.range(0, orFilter.getSubFilters().size())
          .boxed()
          .map(i -> toQuery(index + "_" + i, orFilter.getSubFilters().get(i), adapter, typeCaster))
          .map(e -> {
            e.getValue().map().forEach(parameters::and);
            return e.getKey();
          })
          .map(q -> String.format("( %s )", q))
          .collect(Collectors.joining(" OR "));
      return new SimpleEntry<>(orQuery, parameters);
    } else if (filter instanceof AndFilter andFilter) {
      String andQuery = IntStream.range(0, andFilter.getSubFilters().size())
          .boxed()
          .map(i -> toQuery(index + "_" + i, andFilter.getSubFilters().get(i), adapter, typeCaster))
          .map(e -> {
            e.getValue().map().forEach(parameters::and);
            return e.getKey();
          })
          .map(q -> String.format("( %s )", q))
          .collect(Collectors.joining(" AND "));
      return new SimpleEntry<>(andQuery, parameters);
    } else if (filter instanceof SimpleFilter simpleFilter) {
      return toQuery(index, simpleFilter, adapter, typeCaster);
    }
    throw new UnsupportedOperationException();
  }

  private Entry<String, Parameters> toQuery(
      String index,
      SimpleFilter filter,
      Function<String, String> adapter,
      BiFunction<String, Object, Object> typeCaster
  ) {
    Parameters parameters = new Parameters();
    String query = toQuery(
        Optional.ofNullable(adapter).orElse(Function.identity()),
        Optional.ofNullable(typeCaster).orElse(DefaultTypeCaster::cast),
        index,
        filter.name(),
        filter.operator(),
        filter.value(),
        parameters
    );
    return new SimpleEntry<>(query, parameters);
  }

  private String toQuery(
      Function<String, String> adapter,
      BiFunction<String, Object, Object> typeCaster,
      String index,
      String name,
      Operator operator,
      Object value,
      Parameters parameters
  ) {
    String valueKey = toValueKey(index, name, operator);
    switch (operator) {
      case NOT_EQUAL -> {
        Object castValue = typeCaster.apply(name, value);
        parameters.and(valueKey, castValue);
        return String.format("%s != :%s", adapter.apply(name), valueKey);
      }
      case EQUAL -> {
        Object castValue = typeCaster.apply(name, value);
        parameters.and(valueKey, castValue);
        return String.format("%s = :%s", adapter.apply(name), valueKey);
      }
      case LIKE -> {
        parameters.and(valueKey, String.format("%%%s%%", value.toString()));
        return String.format("lower(%s) LIKE lower(:%s)", adapter.apply(name), valueKey);
      }
      case IS_EMPTY -> {
        return String.format("%s IS EMPTY", adapter.apply(name));
      }
      case IS_NULL -> {
        return String.format("%s IS NULL", adapter.apply(name));
      }
      case LESS_THAN_OR_EQUAL -> {
        Object castValue = typeCaster.apply(name, value);
        parameters.and(valueKey, castValue);
        return String.format("%s <= :%s", adapter.apply(name), valueKey);
      }
      case GREATER_THAN_OR_EQUAL -> {
        Object castValue = typeCaster.apply(name, value);
        parameters.and(valueKey, castValue);
        return String.format("%s >= :%s", adapter.apply(name), valueKey);
      }
      case IN -> {
        Object castValues = toList(typeCaster, name, value);
        parameters.and(valueKey, castValues);
        return String.format("%s IN (:%s)", adapter.apply(name), valueKey);
      }
      default -> throw new UnsupportedOperationException(
          String.format("Operator %s not supported.", operator.name()));
    }
  }

  private Object toList(BiFunction<String, Object, Object> typeCaster, String name, Object value) {
    if (!(value instanceof String)) {
      return value;
    }
    return Arrays.stream(((String) value).split(LIST_SEPARATOR))
        .map(s -> typeCaster.apply(name, s))
        .toList();
  }

  private String toValueKey(String index, String name, Operator operator) {
    return Optional.ofNullable(name)
        .map(s -> s.replace(".", "_"))
        .map(s -> Optional.ofNullable(operator)
            .map(Operator::getValue)
            .orElseGet(() -> RandomStringUtils.secure().nextAlphabetic(8)) + "_" + s + "_" + index)
        .orElseGet(() -> RandomStringUtils.secure().nextAlphabetic(8));
  }

  public Optional<Page> toPage(Paging paging) {
    return Optional.ofNullable(paging)
        .filter(p -> !p.isEmpty())
        .map(p -> Page.of(p.page(), p.size()));
  }

  public Sort toSort(Sorting sorting) {
    return toSort(sorting, Function.identity());
  }

  public Sort toSort(Sorting sorting, Function<String, String> adapter) {
    Sort sort = Sort.empty();
    Optional.ofNullable(sorting).ifPresent(
        s -> s.toList().forEach(
            c -> sort.and(
                adapter.apply(c.name()),
                Direction.valueOf(c.direction().name()),
                NullPrecedence.NULLS_LAST
            )
        ));
    return sort;
  }
}
