package vn.id.thongdanghoang.sep.prodcat.domain.repository.model;

import java.util.List;


public record Paging(
    int page,
    int size
) {

  public static Paging empty() {
    return Paging.of(-1, -1);
  }

  public static Paging of(int page, int size) {
    return new Paging(page, size);
  }

  public boolean isEmpty() {
    return this.equals(Paging.empty());
  }

  /**
   *
   * @param paging
   * @param pages - the total number of pages to be read using the current page size
   * @param count - the total number of items this query operates on
   * @param items
   * @param <T>
   */
  public record Page<T>(
      Paging paging,
      long pages,
      long count,
      List<T> items
  ) {

  }
}
