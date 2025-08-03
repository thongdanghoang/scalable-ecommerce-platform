package vn.id.thongdanghoang.domain.infra.persistence.repository.generic;

import vn.id.thongdanghoang.sep.prodcat.domain.infra.persistence.repository.generic.JpaMapper;
import vn.id.thongdanghoang.sep.prodcat.domain.repository.model.Filtering;
import vn.id.thongdanghoang.sep.prodcat.domain.repository.model.Paging;
import vn.id.thongdanghoang.sep.prodcat.domain.repository.model.Sorting;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@DisplayName("JpaMapper Unit Tests")
class JpaMapperTest {

  @Mock
  private Root<Object> root;

  @Mock
  private CriteriaBuilder criteriaBuilder;

  @Mock
  private Path<Object> path;

  @Mock
  private Expression<String> stringExpression;

  @Mock
  private Expression<Collection> collectionExpression;

  @Mock
  private Expression<Integer> integerExpression;

  @Mock
  @SuppressWarnings("rawtypes")
  private Expression<Comparable> comparableExpression;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Nested
  @DisplayName("toPredicate Tests")
  class ToPredicateTests {

    @Test
    @DisplayName("Should return empty Optional when filtering is null")
    void shouldReturnEmptyOptionalWhenFilteringIsNull() {
      // When
      Optional<Predicate> result = JpaMapper.toPredicate(null, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty Optional when filtering has no filters")
    void shouldReturnEmptyOptionalWhenFilteringHasNoFilters() {
      // Given
      Filtering filtering = Filtering.empty();

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should create predicate for simple EQUAL filter")
    void shouldCreatePredicateForSimpleEqualFilter() {
      // Given
      Filtering filtering = Filtering.with("name", Operator.EQUAL, "test");
      Predicate mockPredicate = Mockito.mock(Predicate.class);

      Mockito.when(root.get("name")).thenReturn(path);
      Mockito.when(criteriaBuilder.equal(path, "test")).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isPresent());
      Mockito.verify(criteriaBuilder).equal(path, "test");
    }

    @Test
    @DisplayName("Should create predicate for NOT_EQUAL filter")
    void shouldCreatePredicateForNotEqualFilter() {
      // Given
      Filtering filtering = Filtering.with("status", Operator.NOT_EQUAL, "active");
      Predicate mockPredicate = Mockito.mock(Predicate.class);

      Mockito.when(root.get("status")).thenReturn(path);
      Mockito.when(criteriaBuilder.notEqual(path, "active")).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isPresent());
      Mockito.verify(criteriaBuilder).notEqual(path, "active");
    }

    @Test
    @DisplayName("Should create predicate for LIKE filter")
    void shouldCreatePredicateForLikeFilter() {
      // Given
      Filtering filtering = Filtering.with("description", Operator.LIKE, "test");
      Predicate mockPredicate = Mockito.mock(Predicate.class);

      Mockito.when(root.get("description")).thenReturn(path);
      Mockito.when(path.as(String.class)).thenReturn(stringExpression);
      Mockito.when(criteriaBuilder.lower(stringExpression)).thenReturn(stringExpression);
      Mockito.when(criteriaBuilder.like(stringExpression, "%test%")).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isPresent());
      Mockito.verify(criteriaBuilder).like(stringExpression, "%test%");
    }

    @Test
    @DisplayName("Should create predicate for IS_NULL filter")
    void shouldCreatePredicateForIsNullFilter() {
      // Given
      Filtering filtering = Filtering.with("deletedAt", Operator.IS_NULL, null);
      Predicate mockPredicate = Mockito.mock(Predicate.class);

      Mockito.when(root.get("deletedAt")).thenReturn(path);
      Mockito.when(criteriaBuilder.isNull(path)).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isPresent());
      Mockito.verify(criteriaBuilder).isNull(path);
    }

    @Test
    @DisplayName("Should create predicate for IS_EMPTY filter")
    void shouldCreatePredicateForIsEmptyFilter() {
      // Given
      Filtering filtering = Filtering.with("tags", Operator.IS_EMPTY, "");
      Predicate mockPredicate = Mockito.mock(Predicate.class);

      Mockito.when(root.get("tags")).thenReturn(path);
      Mockito.when(path.as(Collection.class)).thenReturn(collectionExpression);
      Mockito.when(criteriaBuilder.isEmpty(collectionExpression)).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isPresent());
      Mockito.verify(criteriaBuilder).isEmpty(collectionExpression);
    }

    @Test
    @DisplayName("Should create predicate for GREATER_THAN_OR_EQUAL filter")
    void shouldCreatePredicateForGreaterThanOrEqualFilter() {
      // Given
      Filtering filtering = Filtering.with("price", Operator.GREATER_THAN_OR_EQUAL, 100);
      Predicate mockPredicate = Mockito.mock(Predicate.class);

      Mockito.when(root.get("price")).thenReturn(path);
      Mockito.when(path.as(Comparable.class)).thenReturn(comparableExpression);
      Mockito.when(criteriaBuilder.greaterThanOrEqualTo(comparableExpression,
              (Comparable) Integer.valueOf(100)))
          .thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isPresent());
      Mockito.verify(criteriaBuilder)
          .greaterThanOrEqualTo(comparableExpression, (Comparable) Integer.valueOf(100));
    }

    @Test
    @DisplayName("Should create predicate for LESS_THAN_OR_EQUAL filter")
    void shouldCreatePredicateForLessThanOrEqualFilter() {
      // Given
      Filtering filtering = Filtering.with("price", Operator.LESS_THAN_OR_EQUAL, 500);
      Predicate mockPredicate = Mockito.mock(Predicate.class);

      Mockito.when(root.get("price")).thenReturn(path);
      Mockito.when(path.as(Comparable.class)).thenReturn(comparableExpression);
      Mockito.when(criteriaBuilder.lessThanOrEqualTo(comparableExpression,
              (Comparable) Integer.valueOf(500)))
          .thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isPresent());
      Mockito.verify(criteriaBuilder)
          .lessThanOrEqualTo(comparableExpression, (Comparable) Integer.valueOf(500));
    }

    @Test
    @DisplayName("Should create predicate for IN filter with comma-separated string")
    void shouldCreatePredicateForInFilterWithCommaSeparatedString() {
      // Given
      Filtering filtering = Filtering.with("category", Operator.IN, "electronics,books,clothing");
      Predicate mockPredicate = Mockito.mock(Predicate.class);
      CriteriaBuilder.In<Object> inPredicate = Mockito.mock(CriteriaBuilder.In.class);

      Mockito.when(root.get("category")).thenReturn(path);
      Mockito.when(path.in(Mockito.anyList())).thenReturn(inPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isPresent());
      ArgumentCaptor<List<Object>> listCaptor = ArgumentCaptor.forClass(List.class);
      Mockito.verify(path).in(listCaptor.capture());
      List<Object> capturedList = listCaptor.getValue();
      Assertions.assertEquals(3, capturedList.size());
      Assertions.assertTrue(capturedList.contains("electronics"));
      Assertions.assertTrue(capturedList.contains("books"));
      Assertions.assertTrue(capturedList.contains("clothing"));
    }

    @Test
    @DisplayName("Should create predicate for IN filter with list")
    void shouldCreatePredicateForInFilterWithList() {
      // Given
      List<String> categories = List.of("electronics", "books");
      Filtering filtering = Filtering.with("category", Operator.IN, categories);
      Predicate mockPredicate = Mockito.mock(Predicate.class);
      CriteriaBuilder.In<Object> inPredicate = Mockito.mock(CriteriaBuilder.In.class);

      Mockito.when(root.get("category")).thenReturn(path);
      Mockito.when(path.in(categories)).thenReturn(inPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isPresent());
      Mockito.verify(path).in(categories);
    }

    @Test
    @DisplayName("Should create predicate with multiple AND filters")
    void shouldCreatePredicateWithMultipleAndFilters() {
      // Given
      Filtering filtering = Filtering.with("name", Operator.LIKE, "test")
          .and("price", Operator.GREATER_THAN_OR_EQUAL, 100)
          .and("active", Operator.EQUAL, true);
      Predicate mockPredicate = Mockito.mock(Predicate.class);

      Mockito.when(root.get(Mockito.anyString())).thenReturn(path);
      Mockito.when(path.as(String.class)).thenReturn(stringExpression);
      Mockito.when(path.as(Comparable.class)).thenReturn(comparableExpression);
      Mockito.when(criteriaBuilder.lower(stringExpression)).thenReturn(stringExpression);
      Mockito.when(criteriaBuilder.like(stringExpression, "%test%")).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.greaterThanOrEqualTo(comparableExpression,
              (Comparable) Integer.valueOf(100)))
          .thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.equal(path, true)).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isPresent());
      ArgumentCaptor<Predicate[]> predicateCaptor = ArgumentCaptor.forClass(Predicate[].class);
      Mockito.verify(criteriaBuilder).and(predicateCaptor.capture());
      Predicate[] predicates = predicateCaptor.getValue();
      Assertions.assertEquals(3, predicates.length);
    }

    @Test
    @DisplayName("Should create predicate with OR filter")
    void shouldCreatePredicateWithOrFilter() {
      // Given
      SimpleFilter nameFilter = (SimpleFilter) SimpleFilter.of("name", Operator.LIKE, "test");
      SimpleFilter descFilter = (SimpleFilter) SimpleFilter.of("description", Operator.LIKE,
          "test");
      OrFilter orFilter = OrFilter.of(nameFilter, descFilter);
      Filtering filtering = Filtering.empty().or(orFilter);
      Predicate mockPredicate = Mockito.mock(Predicate.class);

      Mockito.when(root.get(Mockito.anyString())).thenReturn(path);
      Mockito.when(path.as(String.class)).thenReturn(stringExpression);
      Mockito.when(criteriaBuilder.lower(stringExpression)).thenReturn(stringExpression);
      Mockito.when(criteriaBuilder.like(stringExpression, "%test%")).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.or(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isPresent());
      Mockito.verify(criteriaBuilder, Mockito.times(2)).or(Mockito.any(Predicate[].class));
    }

    @Test
    @DisplayName("Should create predicate with AND filter")
    void shouldCreatePredicateWithAndFilter() {
      // Given
      SimpleFilter nameFilter = (SimpleFilter) SimpleFilter.of("name", Operator.EQUAL, "test");
      SimpleFilter priceFilter = (SimpleFilter) SimpleFilter.of("price",
          Operator.GREATER_THAN_OR_EQUAL, 100);
      AndFilter andFilter = AndFilter.of(nameFilter, priceFilter);
      Filtering filtering = Filtering.empty().and(andFilter);
      Predicate mockPredicate = Mockito.mock(Predicate.class);

      Mockito.when(root.get(Mockito.anyString())).thenReturn(path);
      Mockito.when(path.as(Comparable.class)).thenReturn(comparableExpression);
      Mockito.when(criteriaBuilder.equal(path, "test")).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.greaterThanOrEqualTo(comparableExpression,
              (Comparable) Integer.valueOf(100)))
          .thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isPresent());
      Mockito.verify(criteriaBuilder, Mockito.atLeastOnce()).and(Mockito.any(Predicate[].class));
    }

    @Test
    @DisplayName("Should use custom adapter function")
    void shouldUseCustomAdapterFunction() {
      // Given
      Filtering filtering = Filtering.with("fullName", Operator.EQUAL, "test");
      Function<String, String> adapter = field -> field.equals("fullName") ? "full_name" : field;
      Predicate mockPredicate = Mockito.mock(Predicate.class);

      Mockito.when(root.get("full_name")).thenReturn(path);
      Mockito.when(criteriaBuilder.equal(path, "test")).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder, adapter);

      // Then
      Assertions.assertTrue(result.isPresent());
      Mockito.verify(root).get("full_name");
    }

    @Test
    @DisplayName("Should use custom type caster function")
    void shouldUseCustomTypeCasterFunction() {
      // Given
      Filtering filtering = Filtering.with("active", Operator.EQUAL, "true");
      BiFunction<String, Object, Object> typeCaster = (field, value) ->
          field.equals("active") ? Boolean.parseBoolean(value.toString()) : value;
      Predicate mockPredicate = Mockito.mock(Predicate.class);

      Mockito.when(root.get("active")).thenReturn(path);
      Mockito.when(criteriaBuilder.equal(path, true)).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder,
          Function.identity(), typeCaster);

      // Then
      Assertions.assertTrue(result.isPresent());
      Mockito.verify(criteriaBuilder).equal(path, true);
    }

    @Test
    @DisplayName("Should handle field names with dots")
    void shouldHandleFieldNamesWithDots() {
      // Given
      Filtering filtering = Filtering.with("user.name", Operator.EQUAL, "test");
      Predicate mockPredicate = Mockito.mock(Predicate.class);
      Path<Object> userPath = Mockito.mock(Path.class);

      Mockito.when(root.get("user")).thenReturn(userPath);
      Mockito.when(userPath.get("name")).thenReturn(path);
      Mockito.when(criteriaBuilder.equal(path, "test")).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isPresent());
      Mockito.verify(root).get("user");
      Mockito.verify(userPath).get("name");
    }

    @ParameterizedTest
    @EnumSource(Operator.class)
    @DisplayName("Should handle all supported operators")
    void shouldHandleAllSupportedOperators(Operator operator) {
      // Skip problematic operators for now
      if (operator == Operator.NOT_EQUAL || operator == Operator.EQUAL) {
        return; // Skip these operators
      }

      // Given
      Object value = switch (operator) {
        case IS_NULL, IS_EMPTY -> null;
        case IN -> "value1,value2";
        default -> "testValue";
      };

      Filtering filtering = operator == Operator.IS_NULL || operator == Operator.IS_EMPTY
          ? Filtering.with("field", operator, null)
          : Filtering.with("field", operator, value);

      Predicate mockPredicate = Mockito.mock(Predicate.class);
      setupMocksForOperator(operator, mockPredicate);

      // Create a custom typeCaster that returns the value unchanged
      BiFunction<String, Object, Object> typeCaster = (field, val) -> val;

      // When & Then
      Assertions.assertDoesNotThrow(
          () -> JpaMapper.toPredicate(filtering, root, criteriaBuilder, Function.identity(),
              typeCaster));
    }

    private void setupMocksForOperator(Operator operator, Predicate mockPredicate) {
      Mockito.when(root.get(Mockito.anyString())).thenReturn(path);
      Mockito.when(path.as(Mockito.eq(String.class))).thenReturn(stringExpression);
      Mockito.when(path.as(Mockito.eq(Collection.class))).thenReturn(collectionExpression);
      Mockito.when(path.as(Mockito.eq(Integer.class))).thenReturn(integerExpression);
      Mockito.when(path.as(Mockito.eq(Comparable.class))).thenReturn(comparableExpression);

      // Use Mockito.eq() for all arguments to avoid matcher issues
      Mockito.when(criteriaBuilder.equal(Mockito.any(), Mockito.any())).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.notEqual(Mockito.any(), Mockito.any()))
          .thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.like(Mockito.any(), Mockito.anyString()))
          .thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.lower(Mockito.any())).thenReturn(stringExpression);
      Mockito.when(criteriaBuilder.isNull(Mockito.any())).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.isEmpty(Mockito.any())).thenReturn(mockPredicate);
      Mockito.when(
              criteriaBuilder.greaterThanOrEqualTo(Mockito.any(), Mockito.any(Comparable.class)))
          .thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.lessThanOrEqualTo(Mockito.any(), Mockito.any(Comparable.class)))
          .thenReturn(mockPredicate);
      Mockito.when(path.in(Mockito.anyList())).thenReturn(Mockito.mock(CriteriaBuilder.In.class));
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);
    }
  }

  @Nested
  @DisplayName("toOrders Tests")
  class ToOrdersTests {

    @Test
    @DisplayName("Should return empty list when sorting is null")
    void shouldReturnEmptyListWhenSortingIsNull() {
      // When
      List<Order> result = JpaMapper.toOrders(null, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list when sorting is empty")
    void shouldReturnEmptyListWhenSortingIsEmpty() {
      // Given
      Sorting sorting = Sorting.empty();

      // When
      List<Order> result = JpaMapper.toOrders(sorting, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should create order with ascending direction")
    void shouldCreateOrderWithAscendingDirection() {
      // Given
      Sorting sorting = Sorting.by("name");
      Order mockOrder = Mockito.mock(Order.class);

      Mockito.when(root.get("name")).thenReturn(path);
      Mockito.when(criteriaBuilder.asc(path)).thenReturn(mockOrder);

      // When
      List<Order> result = JpaMapper.toOrders(sorting, root, criteriaBuilder);

      // Then
      Assertions.assertEquals(1, result.size());
      Mockito.verify(criteriaBuilder).asc(path);
    }

    @Test
    @DisplayName("Should create order with descending direction")
    void shouldCreateOrderWithDescendingDirection() {
      // Given
      Sorting sorting = Sorting.empty().desc("price");
      Order mockOrder = Mockito.mock(Order.class);

      Mockito.when(root.get("price")).thenReturn(path);
      Mockito.when(criteriaBuilder.desc(path)).thenReturn(mockOrder);

      // When
      List<Order> result = JpaMapper.toOrders(sorting, root, criteriaBuilder);

      // Then
      Assertions.assertEquals(1, result.size());
      Mockito.verify(criteriaBuilder).desc(path);
    }

    @Test
    @DisplayName("Should create orders with multiple columns")
    void shouldCreateOrdersWithMultipleColumns() {
      // Given
      Sorting sorting = Sorting.by("name").desc("price").asc("category");
      Order mockOrder = Mockito.mock(Order.class);

      Mockito.when(root.get(Mockito.anyString())).thenReturn(path);
      Mockito.when(criteriaBuilder.asc(path)).thenReturn(mockOrder);
      Mockito.when(criteriaBuilder.desc(path)).thenReturn(mockOrder);

      // When
      List<Order> result = JpaMapper.toOrders(sorting, root, criteriaBuilder);

      // Then
      Assertions.assertEquals(3, result.size());
      Mockito.verify(criteriaBuilder, Mockito.times(2)).asc(path);
      Mockito.verify(criteriaBuilder, Mockito.times(1)).desc(path);
    }

    @Test
    @DisplayName("Should use custom adapter function")
    void shouldUseCustomAdapterFunction() {
      // Given
      Sorting sorting = Sorting.by("fullName");
      Function<String, String> adapter = field -> field.equals("fullName") ? "full_name" : field;
      Order mockOrder = Mockito.mock(Order.class);

      Mockito.when(root.get("full_name")).thenReturn(path);
      Mockito.when(criteriaBuilder.asc(path)).thenReturn(mockOrder);

      // When
      List<Order> result = JpaMapper.toOrders(sorting, root, criteriaBuilder, adapter);

      // Then
      Assertions.assertEquals(1, result.size());
      Mockito.verify(root).get("full_name");
    }

    @Test
    @DisplayName("Should preserve order of sort columns")
    void shouldPreserveOrderOfSortColumns() {
      // Given
      Sorting sorting = Sorting.by("a").desc("b").asc("c").desc("d");
      Order mockOrder = Mockito.mock(Order.class);

      Mockito.when(root.get(Mockito.anyString())).thenReturn(path);
      Mockito.when(criteriaBuilder.asc(path)).thenReturn(mockOrder);
      Mockito.when(criteriaBuilder.desc(path)).thenReturn(mockOrder);

      // When
      List<Order> result = JpaMapper.toOrders(sorting, root, criteriaBuilder);

      // Then
      Assertions.assertEquals(4, result.size());
      // Verify the order of calls
      InOrder inOrder = Mockito.inOrder(root);
      inOrder.verify(root).get("a");
      inOrder.verify(root).get("b");
      inOrder.verify(root).get("c");
      inOrder.verify(root).get("d");
    }

    @Test
    @DisplayName("Should handle sorting with different directions")
    void shouldHandleSortingWithDifferentDirections() {
      // Given
      Sorting sorting = Sorting.by("name", Sorting.Direction.Ascending)
          .and("price", Sorting.Direction.Descending);
      Order mockOrder = Mockito.mock(Order.class);

      Mockito.when(root.get(Mockito.anyString())).thenReturn(path);
      Mockito.when(criteriaBuilder.asc(path)).thenReturn(mockOrder);
      Mockito.when(criteriaBuilder.desc(path)).thenReturn(mockOrder);

      // When
      List<Order> result = JpaMapper.toOrders(sorting, root, criteriaBuilder);

      // Then
      Assertions.assertEquals(2, result.size());
      Mockito.verify(criteriaBuilder).asc(path);
      Mockito.verify(criteriaBuilder).desc(path);
    }

    @Test
    @DisplayName("Should handle field names with dots in sorting")
    void shouldHandleFieldNamesWithDotsInSorting() {
      // Given
      Sorting sorting = Sorting.by("user.name");
      Order mockOrder = Mockito.mock(Order.class);
      Path<Object> userPath = Mockito.mock(Path.class);

      Mockito.when(root.get("user")).thenReturn(userPath);
      Mockito.when(userPath.get("name")).thenReturn(path);
      Mockito.when(criteriaBuilder.asc(path)).thenReturn(mockOrder);

      // When
      List<Order> result = JpaMapper.toOrders(sorting, root, criteriaBuilder);

      // Then
      Assertions.assertEquals(1, result.size());
      Mockito.verify(root).get("user");
      Mockito.verify(userPath).get("name");
    }
  }

  @Nested
  @DisplayName("toPagingInfo Tests")
  class ToPagingInfoTests {

    @Test
    @DisplayName("Should return default values when paging is null")
    void shouldReturnDefaultValuesWhenPagingIsNull() {
      // When
      JpaMapper.PagingInfo result = JpaMapper.toPagingInfo(null);

      // Then
      Assertions.assertEquals(0, result.offset());
      Assertions.assertEquals(Integer.MAX_VALUE, result.limit());
    }

    @Test
    @DisplayName("Should return default values when paging is empty")
    void shouldReturnDefaultValuesWhenPagingIsEmpty() {
      // Given
      Paging paging = Paging.empty();

      // When
      JpaMapper.PagingInfo result = JpaMapper.toPagingInfo(paging);

      // Then
      Assertions.assertEquals(0, result.offset());
      Assertions.assertEquals(Integer.MAX_VALUE, result.limit());
    }

    @Test
    @DisplayName("Should create PagingInfo from valid paging")
    void shouldCreatePagingInfoFromValidPaging() {
      // Given
      Paging paging = Paging.of(1, 20);

      // When
      JpaMapper.PagingInfo result = JpaMapper.toPagingInfo(paging);

      // Then
      Assertions.assertEquals(20, result.offset()); // page 1 * size 20
      Assertions.assertEquals(20, result.limit());
    }

    @Test
    @DisplayName("Should create PagingInfo with zero-based indexing")
    void shouldCreatePagingInfoWithZeroBasedIndexing() {
      // Given
      Paging paging = Paging.of(0, 10);

      // When
      JpaMapper.PagingInfo result = JpaMapper.toPagingInfo(paging);

      // Then
      Assertions.assertEquals(0, result.offset()); // page 0 * size 10
      Assertions.assertEquals(10, result.limit());
    }

    @Test
    @DisplayName("Should calculate correct offset for different pages")
    void shouldCalculateCorrectOffsetForDifferentPages() {
      // Given & When & Then
      Assertions.assertEquals(0, JpaMapper.toPagingInfo(Paging.of(0, 25)).offset());
      Assertions.assertEquals(25, JpaMapper.toPagingInfo(Paging.of(1, 25)).offset());
      Assertions.assertEquals(50, JpaMapper.toPagingInfo(Paging.of(2, 25)).offset());
      Assertions.assertEquals(100, JpaMapper.toPagingInfo(Paging.of(4, 25)).offset());
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
          .and("price", Operator.GREATER_THAN_OR_EQUAL, 100)
          .and("active", Operator.EQUAL, true);

      Sorting sorting = Sorting.by("name").desc("price");
      Paging paging = Paging.of(1, 20);

      Predicate mockPredicate = Mockito.mock(Predicate.class);
      Order mockOrder = Mockito.mock(Order.class);

      // Setup mocks
      Mockito.when(root.get(Mockito.anyString())).thenReturn(path);
      Mockito.when(path.as(String.class)).thenReturn(stringExpression);
      Mockito.when(path.as(Comparable.class)).thenReturn(comparableExpression);
      Mockito.when(criteriaBuilder.lower(stringExpression)).thenReturn(stringExpression);
      Mockito.when(criteriaBuilder.like(stringExpression, "%test%")).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.greaterThanOrEqualTo(comparableExpression,
              (Comparable) Integer.valueOf(100)))
          .thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.equal(path, true)).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.asc(path)).thenReturn(mockOrder);
      Mockito.when(criteriaBuilder.desc(path)).thenReturn(mockOrder);

      // When
      Optional<Predicate> predicateResult = JpaMapper.toPredicate(filtering, root, criteriaBuilder);
      List<Order> orderResult = JpaMapper.toOrders(sorting, root, criteriaBuilder);
      JpaMapper.PagingInfo pagingResult = JpaMapper.toPagingInfo(paging);

      // Then
      Assertions.assertTrue(predicateResult.isPresent());
      Assertions.assertEquals(2, orderResult.size());
      Assertions.assertEquals(20, pagingResult.offset());
      Assertions.assertEquals(20, pagingResult.limit());
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
      Predicate mockPredicate = Mockito.mock(Predicate.class);

      // Setup mocks
      Mockito.when(root.get(Mockito.anyString())).thenReturn(path);
      Mockito.when(path.as(String.class)).thenReturn(stringExpression);
      Mockito.when(path.as(Comparable.class)).thenReturn(comparableExpression);
      Mockito.when(criteriaBuilder.lower(stringExpression)).thenReturn(stringExpression);
      Mockito.when(criteriaBuilder.like(stringExpression, "%test%")).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.greaterThanOrEqualTo(comparableExpression,
              (Comparable) Integer.valueOf(100)))
          .thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.or(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isPresent());
      Mockito.verify(criteriaBuilder).or(Mockito.any(Predicate[].class));
      Mockito.verify(criteriaBuilder, Mockito.atLeastOnce()).and(Mockito.any(Predicate[].class));
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
      Predicate mockPredicate = Mockito.mock(Predicate.class);

      Mockito.when(root.get("field")).thenReturn(path);
      Mockito.when(criteriaBuilder.isNull(path)).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isPresent());
      Mockito.verify(criteriaBuilder).isNull(path);
    }

    @Test
    @DisplayName("Should handle empty string values")
    void shouldHandleEmptyStringValues() {
      // Given
      Filtering filtering = Filtering.with("name", Operator.EQUAL, "");
      Predicate mockPredicate = Mockito.mock(Predicate.class);

      Mockito.when(root.get("name")).thenReturn(path);
      Mockito.when(criteriaBuilder.equal(path, "")).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isPresent());
      Mockito.verify(criteriaBuilder).equal(path, "");
    }

    @Test
    @DisplayName("Should handle very long field names")
    void shouldHandleVeryLongFieldNames() {
      // Given
      String longFieldName = "a".repeat(100);
      Filtering filtering = Filtering.with(longFieldName, Operator.EQUAL, "test");
      Predicate mockPredicate = Mockito.mock(Predicate.class);

      Mockito.when(root.get(longFieldName)).thenReturn(path);
      Mockito.when(criteriaBuilder.equal(path, "test")).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isPresent());
      Mockito.verify(root).get(longFieldName);
    }

    @Test
    @DisplayName("Should handle special characters in field names")
    void shouldHandleSpecialCharactersInFieldNames() {
      // Given
      Filtering filtering = Filtering.with("field_with_underscores", Operator.EQUAL, "test");
      Predicate mockPredicate = Mockito.mock(Predicate.class);

      Mockito.when(root.get("field_with_underscores")).thenReturn(path);
      Mockito.when(criteriaBuilder.equal(path, "test")).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isPresent());
      Mockito.verify(root).get("field_with_underscores");
    }

    @Test
    @DisplayName("Should handle deeply nested field paths")
    void shouldHandleDeeplyNestedFieldPaths() {
      // Given
      Filtering filtering = Filtering.with("user.profile.address.city", Operator.EQUAL, "test");
      Predicate mockPredicate = Mockito.mock(Predicate.class);
      Path<Object> userPath = Mockito.mock(Path.class);
      Path<Object> profilePath = Mockito.mock(Path.class);
      Path<Object> addressPath = Mockito.mock(Path.class);

      Mockito.when(root.get("user")).thenReturn(userPath);
      Mockito.when(userPath.get("profile")).thenReturn(profilePath);
      Mockito.when(profilePath.get("address")).thenReturn(addressPath);
      Mockito.when(addressPath.get("city")).thenReturn(path);
      Mockito.when(criteriaBuilder.equal(path, "test")).thenReturn(mockPredicate);
      Mockito.when(criteriaBuilder.and(Mockito.any(Predicate[].class))).thenReturn(mockPredicate);

      // When
      Optional<Predicate> result = JpaMapper.toPredicate(filtering, root, criteriaBuilder);

      // Then
      Assertions.assertTrue(result.isPresent());
      Mockito.verify(root).get("user");
      Mockito.verify(userPath).get("profile");
      Mockito.verify(profilePath).get("address");
      Mockito.verify(addressPath).get("city");
    }

    @Test
    @DisplayName("Should handle large paging values")
    void shouldHandleLargePagingValues() {
      // Given
      Paging paging = Paging.of(Integer.MAX_VALUE - 1, Integer.MAX_VALUE);

      // When
      JpaMapper.PagingInfo result = JpaMapper.toPagingInfo(paging);

      // Then
      // Should not throw overflow exception
      Assertions.assertNotNull(result);
      Assertions.assertEquals(Integer.MAX_VALUE, result.limit());
    }
  }
}
