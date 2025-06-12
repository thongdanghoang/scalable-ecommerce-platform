package vn.id.thongdanghoang.domain.infra.persistence.repository.generic;

import vn.id.thongdanghoang.sep.prodcat.domain.infra.persistence.repository.generic.PanacheMapper;
import vn.id.thongdanghoang.sep.prodcat.domain.repository.model.Filtering;
import vn.id.thongdanghoang.sep.prodcat.domain.repository.model.Paging;
import vn.id.thongdanghoang.sep.prodcat.domain.repository.model.Sorting;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import io.quarkus.panache.common.Sort.NullPrecedence;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayName("PanacheMapper Unit Tests")
class PanacheMapperTest {

  @Nested
  @DisplayName("toQuery Tests")
  class ToQueryTests {

    @Test
    @DisplayName("Should return empty Optional when filtering is null")
    void shouldReturnEmptyOptionalWhenFilteringIsNull() {
      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(null);

      // Then
      Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty Optional when filtering has no filters")
    void shouldReturnEmptyOptionalWhenFilteringHasNoFilters() {
      // Given
      Filtering filtering = Filtering.empty();

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering);

      // Then
      Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should create query for simple EQUAL filter")
    void shouldCreateQueryForSimpleEqualFilter() {
      // Given
      Filtering filtering = Filtering.with("name", Operator.EQUAL, "test");

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering);

      // Then
      Assertions.assertTrue(result.isPresent());
      Map.Entry<String, Parameters> entry = result.get();
      Assertions.assertEquals("( name = :eq_name_0 )", entry.getKey());
      Assertions.assertEquals(1, entry.getValue().map().size());
      Assertions.assertEquals("test", entry.getValue().map().get("eq_name_0"));
    }

    @Test
    @DisplayName("Should create query for NOT_EQUAL filter")
    void shouldCreateQueryForNotEqualFilter() {
      // Given
      Filtering filtering = Filtering.with("status", Operator.NOT_EQUAL, "active");

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering);

      // Then
      Assertions.assertTrue(result.isPresent());
      Map.Entry<String, Parameters> entry = result.get();
      Assertions.assertEquals("( status != :ne_status_0 )", entry.getKey());
      Assertions.assertTrue(entry.getValue().map().containsKey("ne_status_0"));
      Assertions.assertEquals("active", entry.getValue().map().get("ne_status_0"));
    }

    @Test
    @DisplayName("Should create query for LIKE filter")
    void shouldCreateQueryForLikeFilter() {
      // Given
      Filtering filtering = Filtering.with("description", Operator.LIKE, "test");

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering);

      // Then
      Assertions.assertTrue(result.isPresent());
      Map.Entry<String, Parameters> entry = result.get();
      Assertions.assertEquals("( lower(description) LIKE lower(:lk_description_0) )",
          entry.getKey());
      Assertions.assertTrue(entry.getValue().map().containsKey("lk_description_0"));
      Assertions.assertEquals("%test%", entry.getValue().map().get("lk_description_0"));
    }

    @Test
    @DisplayName("Should create query for IS_NULL filter")
    void shouldCreateQueryForIsNullFilter() {
      // Given
      Filtering filtering = Filtering.with("deletedAt", Operator.IS_NULL, null);

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering);

      // Then
      Assertions.assertTrue(result.isPresent());
      Map.Entry<String, Parameters> entry = result.get();
      Assertions.assertEquals("( deletedAt IS NULL )", entry.getKey());
      Assertions.assertTrue(entry.getValue().map().isEmpty());
    }

    @Test
    @DisplayName("Should create query for IS_EMPTY filter")
    void shouldCreateQueryForIsEmptyFilter() {
      // Given
      Filtering filtering = Filtering.with("tags", Operator.IS_EMPTY, "");

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering);

      // Then
      Assertions.assertTrue(result.isPresent());
      Map.Entry<String, Parameters> entry = result.get();
      Assertions.assertEquals("( tags IS EMPTY )", entry.getKey());
      Assertions.assertTrue(entry.getValue().map().isEmpty());
    }

    @Test
    @DisplayName("Should create query for GREATER_THAN_OR_EQUAL filter")
    void shouldCreateQueryForGreaterThanOrEqualFilter() {
      // Given
      Filtering filtering = Filtering.with("price", Operator.GREATER_THAN_OR_EQUAL, 100);

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering);

      // Then
      Assertions.assertTrue(result.isPresent());
      Map.Entry<String, Parameters> entry = result.get();
      Assertions.assertEquals("( price >= :gte_price_0 )", entry.getKey());
      Assertions.assertTrue(entry.getValue().map().containsKey("gte_price_0"));
      Assertions.assertEquals(100, entry.getValue().map().get("gte_price_0"));
    }

    @Test
    @DisplayName("Should create query for LESS_THAN_OR_EQUAL filter")
    void shouldCreateQueryForLessThanOrEqualFilter() {
      // Given
      Filtering filtering = Filtering.with("price", Operator.LESS_THAN_OR_EQUAL, 500);

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering);

      // Then
      Assertions.assertTrue(result.isPresent());
      Map.Entry<String, Parameters> entry = result.get();
      Assertions.assertEquals("( price <= :lte_price_0 )", entry.getKey());
      Assertions.assertTrue(entry.getValue().map().containsKey("lte_price_0"));
      Assertions.assertEquals(500, entry.getValue().map().get("lte_price_0"));
    }

    @Test
    @DisplayName("Should create query for IN filter with comma-separated string")
    void shouldCreateQueryForInFilterWithCommaSeparatedString() {
      // Given
      Filtering filtering = Filtering.with("category", Operator.IN, "electronics,books,clothing");

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering);

      // Then
      Assertions.assertTrue(result.isPresent());
      Map.Entry<String, Parameters> entry = result.get();
      Assertions.assertEquals("( category IN (:in_category_0) )", entry.getKey());
      Assertions.assertTrue(entry.getValue().map().containsKey("in_category_0"));
      List<?> values = (List<?>) entry.getValue().map().get("in_category_0");
      Assertions.assertEquals(3, values.size());
      Assertions.assertTrue(values.contains("electronics"));
      Assertions.assertTrue(values.contains("books"));
      Assertions.assertTrue(values.contains("clothing"));
    }

    @Test
    @DisplayName("Should create query for IN filter with list")
    void shouldCreateQueryForInFilterWithList() {
      // Given
      List<String> categories = List.of("electronics", "books");
      Filtering filtering = Filtering.with("category", Operator.IN, categories);

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering);

      // Then
      Assertions.assertTrue(result.isPresent());
      Map.Entry<String, Parameters> entry = result.get();
      Assertions.assertEquals("( category IN (:in_category_0) )", entry.getKey());
      Assertions.assertEquals(categories, entry.getValue().map().get("in_category_0"));
    }

    @Test
    @DisplayName("Should create query with multiple AND filters")
    void shouldCreateQueryWithMultipleAndFilters() {
      // Given
      Filtering filtering = Filtering.with("name", Operator.LIKE, "test")
          .and("price", Operator.GREATER_THAN_OR_EQUAL, 100).and("active", Operator.EQUAL, true);

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering);

      // Then
      Assertions.assertTrue(result.isPresent());
      Map.Entry<String, Parameters> entry = result.get();
      Assertions.assertTrue(entry.getKey().contains("AND"));
      Assertions.assertEquals(3, entry.getValue().map().size());
    }

    @Test
    @DisplayName("Should create query with OR filter")
    void shouldCreateQueryWithOrFilter() {
      // Given
      var nameFilter = SimpleFilter.of("name", Operator.LIKE, "test");
      var descFilter = SimpleFilter.of("description", Operator.LIKE, "test");
      var orFilter = OrFilter.of(nameFilter, descFilter);
      var filtering = Filtering.empty().or(orFilter);

      // When
      var query = PanacheMapper.toQuery(filtering);

      // Then
      Assertions.assertTrue(query.isPresent());
      var entry = query.get();
      Assertions.assertTrue(entry.getKey().contains("OR"));
    }

    @Test
    @DisplayName("Should create query with AND filter")
    void shouldCreateQueryWithAndFilter() {
      // Given
      SimpleFilter nameFilter = (SimpleFilter) SimpleFilter.of("name", Operator.EQUAL, "test");
      SimpleFilter priceFilter = (SimpleFilter) SimpleFilter.of("price",
          Operator.GREATER_THAN_OR_EQUAL, 100);
      AndFilter andFilter = AndFilter.of(nameFilter, priceFilter);
      Filtering filtering = Filtering.empty().and(andFilter);

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering);

      // Then
      Assertions.assertTrue(result.isPresent());
      Map.Entry<String, Parameters> entry = result.get();
      Assertions.assertTrue(entry.getKey().contains("AND"));
    }

    @Test
    @DisplayName("Should use custom adapter function")
    void shouldUseCustomAdapterFunction() {
      // Given
      Filtering filtering = Filtering.with("fullName", Operator.EQUAL, "test");
      Function<String, String> adapter = field -> field.equals("fullName") ? "full_name" : field;

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering, adapter);

      // Then
      Assertions.assertTrue(result.isPresent());
      Map.Entry<String, Parameters> entry = result.get();
      Assertions.assertTrue(entry.getKey().contains("full_name = "));
    }

    @Test
    @DisplayName("Should use custom type caster function")
    void shouldUseCustomTypeCasterFunction() {
      // Given
      Filtering filtering = Filtering.with("active", Operator.EQUAL, "true");
      BiFunction<String, Object, Object> typeCaster = (field, value) -> field.equals("active")
          ? Boolean.parseBoolean(value.toString()) : value;

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering,
          Function.identity(), typeCaster);

      // Then
      Assertions.assertTrue(result.isPresent());
      Map.Entry<String, Parameters> entry = result.get();
      Assertions.assertTrue(entry.getValue().map().containsValue(true));
    }

    @Test
    @DisplayName("Should handle field names with dots")
    void shouldHandleFieldNamesWithDots() {
      // Given
      Filtering filtering = Filtering.with("user.name", Operator.EQUAL, "test");

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering);

      // Then
      Assertions.assertTrue(result.isPresent());
      Map.Entry<String, Parameters> entry = result.get();
      // Parameter key should replace dots with underscores
      Assertions.assertTrue(
          entry.getValue().map().keySet().iterator().next().contains("user_name"));
    }

    @Test
    @DisplayName("Should throw UnsupportedOperationException for unknown filter type")
    void shouldThrowUnsupportedOperationExceptionForUnknownFilterType() {
      // Given
      Filtering.Filter unknownFilter = new Filtering.Filter() {
      };
      Filtering filtering = Filtering.empty().and(unknownFilter);

      // When & Then
      Assertions.assertThrows(UnsupportedOperationException.class,
          () -> PanacheMapper.toQuery(filtering));
    }

    @ParameterizedTest
    @EnumSource(Operator.class)
    @DisplayName("Should handle all supported operators")
    void shouldHandleAllSupportedOperators(Operator operator) {
      // Given
      Object value = switch (operator) {
        case IS_NULL, IS_EMPTY -> null;
        case IN -> "value1,value2";
        default -> "testValue";
      };

      Filtering filtering =
          operator == Operator.IS_NULL || operator == Operator.IS_EMPTY ? Filtering.with("field",
              operator, null) : Filtering.with("field", operator, value);

      // When & Then
      Assertions.assertDoesNotThrow(() -> PanacheMapper.toQuery(filtering));
    }
  }

  @Nested
  @DisplayName("toPage Tests")
  class ToPageTests {

    @Test
    @DisplayName("Should return empty Optional when paging is null")
    void shouldReturnEmptyOptionalWhenPagingIsNull() {
      // When
      Optional<Page> result = PanacheMapper.toPage(null);

      // Then
      Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty Optional when paging is empty")
    void shouldReturnEmptyOptionalWhenPagingIsEmpty() {
      // Given
      Paging paging = Paging.empty();

      // When
      Optional<Page> result = PanacheMapper.toPage(paging);

      // Then
      Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should create Page from valid paging")
    void shouldCreatePageFromValidPaging() {
      // Given
      Paging paging = Paging.of(1, 20);

      // When
      Optional<Page> result = PanacheMapper.toPage(paging);

      // Then
      Assertions.assertTrue(result.isPresent());
      Page page = result.get();
      Assertions.assertEquals(1, page.index);
      Assertions.assertEquals(20, page.size);
    }

    @Test
    @DisplayName("Should create Page with zero-based indexing")
    void shouldCreatePageWithZeroBasedIndexing() {
      // Given
      Paging paging = Paging.of(0, 10);

      // When
      Optional<Page> result = PanacheMapper.toPage(paging);

      // Then
      Assertions.assertTrue(result.isPresent());
      Page page = result.get();
      Assertions.assertEquals(0, page.index);
      Assertions.assertEquals(10, page.size);
    }
  }

  @Nested
  @DisplayName("toSort Tests")
  class ToSortTests {

    @Test
    @DisplayName("Should return empty Sort when sorting is null")
    void shouldReturnEmptySortWhenSortingIsNull() {
      // When
      Sort result = PanacheMapper.toSort(null);

      // Then
      Assertions.assertTrue(CollectionUtils.isEmpty(result.getColumns()));
    }

    @Test
    @DisplayName("Should return empty Sort when sorting is empty")
    void shouldReturnEmptySortWhenSortingIsEmpty() {
      // Given
      Sorting sorting = Sorting.empty();

      // When
      Sort result = PanacheMapper.toSort(sorting);

      // Then
      Assertions.assertTrue(CollectionUtils.isEmpty(result.getColumns()));
    }

    @Test
    @DisplayName("Should create Sort with ascending direction")
    void shouldCreateSortWithAscendingDirection() {
      // Given
      Sorting sorting = Sorting.by("name");

      // When
      Sort result = PanacheMapper.toSort(sorting);

      // Then
      Assertions.assertEquals(1, result.getColumns().size());
      Sort.Column column = result.getColumns().getFirst();
      Assertions.assertEquals("name", column.getName());
      Assertions.assertEquals(Direction.Ascending, column.getDirection());
      Assertions.assertEquals(NullPrecedence.NULLS_LAST, column.getNullPrecedence());
    }

    @Test
    @DisplayName("Should create Sort with descending direction")
    void shouldCreateSortWithDescendingDirection() {
      // Given
      Sorting sorting = Sorting.empty().desc("price");

      // When
      Sort result = PanacheMapper.toSort(sorting);

      // Then
      Assertions.assertEquals(1, result.getColumns().size());
      Sort.Column column = result.getColumns().getFirst();
      Assertions.assertEquals("price", column.getName());
      Assertions.assertEquals(Direction.Descending, column.getDirection());
      Assertions.assertEquals(NullPrecedence.NULLS_LAST, column.getNullPrecedence());
    }

    @Test
    @DisplayName("Should create Sort with multiple columns")
    void shouldCreateSortWithMultipleColumns() {
      // Given
      Sorting sorting = Sorting.by("name").desc("price").asc("category");

      // When
      Sort result = PanacheMapper.toSort(sorting);

      // Then
      Assertions.assertEquals(3, result.getColumns().size());

      Sort.Column nameColumn = result.getColumns().getFirst();
      Assertions.assertEquals("name", nameColumn.getName());
      Assertions.assertEquals(Direction.Ascending, nameColumn.getDirection());

      Sort.Column priceColumn = result.getColumns().get(1);
      Assertions.assertEquals("price", priceColumn.getName());
      Assertions.assertEquals(Direction.Descending, priceColumn.getDirection());

      Sort.Column categoryColumn = result.getColumns().get(2);
      Assertions.assertEquals("category", categoryColumn.getName());
      Assertions.assertEquals(Direction.Ascending, categoryColumn.getDirection());
    }

    @Test
    @DisplayName("Should use custom adapter function")
    void shouldUseCustomAdapterFunction() {
      // Given
      Sorting sorting = Sorting.by("fullName");
      Function<String, String> adapter = field -> field.equals("fullName") ? "full_name" : field;

      // When
      Sort result = PanacheMapper.toSort(sorting, adapter);

      // Then
      Assertions.assertEquals(1, result.getColumns().size());
      Sort.Column column = result.getColumns().getFirst();
      Assertions.assertEquals("full_name", column.getName());
    }

    @Test
    @DisplayName("Should preserve order of sort columns")
    void shouldPreserveOrderOfSortColumns() {
      // Given
      Sorting sorting = Sorting.by("a").desc("b").asc("c").desc("d");

      // When
      Sort result = PanacheMapper.toSort(sorting);

      // Then
      Assertions.assertEquals(4, result.getColumns().size());
      Assertions.assertEquals("a", result.getColumns().get(0).getName());
      Assertions.assertEquals("b", result.getColumns().get(1).getName());
      Assertions.assertEquals("c", result.getColumns().get(2).getName());
      Assertions.assertEquals("d", result.getColumns().get(3).getName());
    }

    @Test
    @DisplayName("Should handle sorting with different directions")
    void shouldHandleSortingWithDifferentDirections() {
      // Given
      Sorting sorting = Sorting.by("name",
              Sorting.Direction.Ascending)
          .and("price", Sorting.Direction.Descending);

      // When
      Sort result = PanacheMapper.toSort(sorting);

      // Then
      Assertions.assertEquals(2, result.getColumns().size());
      Assertions.assertEquals(Direction.Ascending, result.getColumns().get(0).getDirection());
      Assertions.assertEquals(Direction.Descending, result.getColumns().get(1).getDirection());
    }
  }

  @Nested
  @DisplayName("Integration Tests")
  class IntegrationTests {

    @Test
    @DisplayName("Should handle complex filtering with sorting and paging")
    void shouldHandleComplexFilteringWithSortingAndPaging() {
      // Given
      Filtering filtering = Filtering.with("name", Operator.LIKE, "test")
          .and("price", Operator.GREATER_THAN_OR_EQUAL, 100).and("active", Operator.EQUAL, true);

      Sorting sorting = Sorting.by("name").desc("price");
      Paging paging = Paging.of(1, 20);

      // When
      Optional<Map.Entry<String, Parameters>> queryResult = PanacheMapper.toQuery(filtering);
      Sort sortResult = PanacheMapper.toSort(sorting);
      Optional<Page> pageResult = PanacheMapper.toPage(paging);

      // Then
      Assertions.assertTrue(queryResult.isPresent());
      Assertions.assertEquals(3, queryResult.get().getValue().map().size());

      Assertions.assertEquals(2, sortResult.getColumns().size());

      Assertions.assertTrue(pageResult.isPresent());
      Assertions.assertEquals(20, pageResult.get().size);
    }

    @Test
    @DisplayName("Should handle nested OR and AND filters")
    void shouldHandleNestedOrAndAndFilters() {
      // Given
      SimpleFilter nameFilter = (SimpleFilter) SimpleFilter.of("name", Operator.LIKE, "test");
      SimpleFilter descFilter = (SimpleFilter) SimpleFilter.of("description", Operator.LIKE,
          "test");
      OrFilter orFilter = OrFilter.of(nameFilter, descFilter);

      SimpleFilter priceFilter = (SimpleFilter) SimpleFilter.of("price",
          Operator.GREATER_THAN_OR_EQUAL, 100);
      AndFilter andFilter = AndFilter.of(orFilter, priceFilter);

      Filtering filtering = Filtering.empty().and(andFilter);

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering);

      // Then
      Assertions.assertTrue(result.isPresent());
      Map.Entry<String, Parameters> entry = result.get();
      Assertions.assertTrue(entry.getKey().contains("AND"));
      Assertions.assertTrue(entry.getKey().contains("OR"));
      Assertions.assertEquals(3, entry.getValue().map().size());
    }

    @Test
    @DisplayName("Should generate unique parameter keys for similar field names")
    void shouldGenerateUniqueParameterKeysForSimilarFieldNames() {
      // Given
      Filtering filtering = Filtering.with("name", Operator.EQUAL, "test1")
          .and("name", Operator.NOT_EQUAL, "test2");

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering);

      // Then
      Assertions.assertTrue(result.isPresent());
      Map.Entry<String, Parameters> entry = result.get();
      Assertions.assertEquals(2, entry.getValue().map().size());
      Assertions.assertEquals(2, entry.getValue().map().size()); // Should have unique keys
    }
  }

  @Nested
  @DisplayName("Edge Cases")
  class EdgeCasesTests {

    @Test
    @DisplayName("Should handle null values in filters appropriately")
    void shouldHandleNullValuesInFiltersAppropriately() {
      // Given
      Filtering filtering = Filtering.with("field", Operator.IS_NULL, null);

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering);

      // Then
      Assertions.assertTrue(result.isPresent());
      Map.Entry<String, Parameters> entry = result.get();
      Assertions.assertEquals("( field IS NULL )", entry.getKey());
      Assertions.assertTrue(entry.getValue().map().isEmpty());
    }

    @Test
    @DisplayName("Should handle empty string values")
    void shouldHandleEmptyStringValues() {
      // Given
      Filtering filtering = Filtering.with("name", Operator.EQUAL, "");

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering);

      // Then
      Assertions.assertTrue(result.isPresent());
      Map.Entry<String, Parameters> entry = result.get();
      Assertions.assertTrue(entry.getValue().map().containsValue(""));
    }

    @Test
    @DisplayName("Should handle very long field names")
    void shouldHandleVeryLongFieldNames() {
      // Given
      String longFieldName = "a".repeat(100);
      Filtering filtering = Filtering.with(longFieldName, Operator.EQUAL, "test");

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering);

      // Then
      Assertions.assertTrue(result.isPresent());
      Map.Entry<String, Parameters> entry = result.get();
      Assertions.assertTrue(entry.getKey().contains(longFieldName));
    }

    @Test
    @DisplayName("Should handle special characters in field names")
    void shouldHandleSpecialCharactersInFieldNames() {
      // Given
      Filtering filtering = Filtering.with("field_with_underscores", Operator.EQUAL, "test");

      // When
      Optional<Map.Entry<String, Parameters>> result = PanacheMapper.toQuery(filtering);

      // Then
      Assertions.assertTrue(result.isPresent());
      Map.Entry<String, Parameters> entry = result.get();
      Assertions.assertTrue(entry.getKey().contains("field_with_underscores"));
    }
  }
}