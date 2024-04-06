package thongdanghoang.id.vn.n3tkproduct.common.dtos;

import lombok.Data;

/**
 * Base criteria wrapper class for search feature and table in UI.
 *
 * @param <T> Criteria class. It may contain single field (String, Number,...) or composite fields (custom criteria)
 */
@Data
public class SearchCriteriaDto<T> {
    private SearchPageDto page;
    private T criteria;
    private SortDto sort;
}
