package vn.id.thongdanghoang.n3tk.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

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
    private long totalElements;
    private int totalPages;

    public static <T> SearchResultDto<T> of(Page<T> page) {
        return new SearchResultDto<>(page.getContent(), page.getTotalElements(), page.getTotalPages());
    }

}
