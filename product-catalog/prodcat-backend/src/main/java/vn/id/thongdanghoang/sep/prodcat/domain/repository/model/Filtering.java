package vn.id.thongdanghoang.sep.prodcat.domain.repository.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * <p>This class aims to support filtering requests coming from the API under the form:
 * https://{hostname}/{api}/resources?filter=name:lk:value&filter=total_points:gt:500.</p>
 *
 * <p>The filters list resulting in these query params should be parsed and transformed into a
 * Filtering model that will then be converted and interpreted by the persistence layer. This syntax
 * gives a maximum usage flexibility. The list of `Operator`s is not fully defined yet.</p>
 *
 * <p>At the service level instantiating a specific filter is pretty simple:<br/>
 * <code> Filtering.with(List.of("name", "visa"), Operator.LIKE, "batman").and("totalPoints",
 * Operator.EQUAL, 10)</code><br/> will result into the logical filter:<br/> (name like "%batman%"
 * OR visa like "%batman%") AND totalPoints = 10.</p>
 */
@Value
public class Filtering {

  List<Filter> filters = new ArrayList<>();

  public static Filtering empty() {
    return new Filtering();
  }

  public static Filtering with(String name, Operator operator, Object value) {
    return (new Filtering()).and(name, operator, value);
  }

  private static void assertCoherency(Operator operator, Object value) {
    if (!Set.of(Operator.IS_EMPTY, Operator.IS_NULL).contains(operator) && Objects.isNull(value)) {
      throw new IllegalArgumentException(
          String.format("Operator %s require a value.", operator.name()));
    }
  }

  public Filtering or(Filter... filters) {
    this.filters.add(OrFilter.builder().subFilters(Arrays.asList(filters)).build());
    return this;
  }

  public Filtering and(Filtering filtering) {
    this.filters.addAll(filtering.filters);
    return this;
  }

  public Filtering and(Filter... filters) {
    this.filters.addAll(Arrays.asList(filters));
    return this;
  }

  public Filtering and(String name, Operator operator) {
    return this.and(name, operator, null);
  }

  public Filtering and(String name, Operator operator, Object value) {
    this.filters.add(SimpleFilter.of(name, operator, value));
    return this;
  }

  public List<Filter> toList() {
    return Collections.unmodifiableList(this.filters);
  }

  @Getter
  @RequiredArgsConstructor
  public enum Operator {
    NOT_EQUAL("ne"),
    EQUAL("eq"),
    LIKE("lk"),
    IS_EMPTY("mty"),
    IS_NULL("nul"),
    LESS_THAN_OR_EQUAL("lte"),
    GREATER_THAN_OR_EQUAL("gte"),
    IN("in");

    private final String value;

    public static Operator of(String value) {
      return Arrays.stream(Operator.values())
          .filter(o -> o.value.equals(value))
          .findFirst()
          .orElseThrow(() -> new UnsupportedOperationException(
              String.format("Operator %s do not exists.", value)
          ));
    }
  }

  public interface Filter {

  }

  @Data
  @Builder
  public static class AndFilter implements Filter {

    List<Filter> subFilters;

    public static AndFilter of(Filter... filters) {
      return new AndFilter(Arrays.asList(filters));
    }
  }

  @Data
  @Builder
  public static class OrFilter implements Filter {

    List<Filter> subFilters;

    public static OrFilter of(Filter... filters) {
      return new OrFilter(Arrays.asList(filters));
    }
  }

  public record SimpleFilter(
      @NotEmpty String name,
      @NotNull Operator operator,
      Object value
  ) implements Filter {

    public static Filter of(String name, Operator operator, Object value) {
      assertCoherency(operator, value);
      return new SimpleFilter(name, operator, value);
    }

    public static Filter of(String name, Operator operator) {
      return of(name, operator, null);
    }
  }
}
