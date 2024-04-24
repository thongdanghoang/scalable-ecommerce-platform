package vn.id.thongdanghoang.n3tk.common.dtos;

import lombok.Data;

/**
 * Search page dto class, used for search feature and table component in UI
 */
@Data
public class SearchPageDto {
    private int pageNumber;
    private int pageSize;
    
    /**
     * @param pageNumber zero-based page number, must not be negative.
     * @param pageSize the size of the page to be returned, must be greater than 0.
     * @return new instance of SearchPageDto
     */
    public static SearchPageDto of(int pageNumber, int pageSize) {
        SearchPageDto searchPageDto = new SearchPageDto();
        searchPageDto.setPageNumber(pageNumber);
        searchPageDto.setPageNumber(pageSize);
        return searchPageDto;
    }
}
