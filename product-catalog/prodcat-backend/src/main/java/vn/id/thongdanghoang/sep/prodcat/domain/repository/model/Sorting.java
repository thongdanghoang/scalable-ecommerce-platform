package vn.id.thongdanghoang.sep.prodcat.domain.repository.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * <p>This class aims to support sorting requests coming from the API under the form:
 * <code>https://{hostname}/{api}/resources?sort=-total_points&sort=+name</code>.</p>
 *
 * <p>The sorting list resulting in these query params should be parsed and transformed into a
 * Sorting model that will then be converted and interpreted by the persistence layer. This syntax
 * gives a maximum usage flexibility.</p>
 *
 * <p>At the service level instantiating a specific sort is pretty simple:<br/>
 * <code>Sorting.by("name").desc("totalPoints")</code><br/>
 * will result into the logical filter:<br/> name asc, totalPoints desc.</p>
 */
@Value
public class Sorting {

  List<Column> columns = new ArrayList<>();

  private Sorting() {
  }

  public static Sorting empty() {
    return new Sorting();
  }

  public static Sorting by(String column) {
    return Sorting.empty().asc(column);
  }

  public static Sorting by(String column, Direction direction) {
    return Sorting.empty().and(column, direction);
  }

  public List<Column> toList() {
    return Collections.unmodifiableList(this.columns);
  }

  public Sorting and(String name, Direction direction) {
    this.columns.add(Column.builder().name(name).direction(direction).build());
    return this;
  }

  public Sorting asc(String name) {
    this.columns.add(Column.builder().name(name).direction(Direction.Ascending).build());
    return this;
  }

  public Sorting desc(String name) {
    this.columns.add(Column.builder().name(name).direction(Direction.Descending).build());
    return this;
  }

  @RequiredArgsConstructor
  public enum Direction {
    Ascending('+'),
    Descending('-');

    private final char value;

    public static Direction of(char value) {
      return Arrays.stream(Direction.values())
          .filter(o -> o.value == value)
          .findFirst()
          .orElseThrow(() -> new UnsupportedOperationException(
              String.format("Direction %s do not exists.", value)
          ));
    }
  }

  @Builder
  public record Column(
      @NotBlank String name,
      @NotNull Direction direction
  ) {

  }
}
