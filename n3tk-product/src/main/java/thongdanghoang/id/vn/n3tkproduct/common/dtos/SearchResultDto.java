package thongdanghoang.id.vn.n3tkproduct.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Search result dto that is returned from search feature. It is used for table in UI
 *
 * @param <T> Result dto class
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDto<T> {
    private List<T> results;
    private long total;
}
