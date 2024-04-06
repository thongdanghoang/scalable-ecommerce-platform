package thongdanghoang.id.vn.n3tkproduct.common.dtos;

import lombok.Data;

/**
 * Search page dto class, used for search feature and table component in UI
 */
@Data
public class SearchPageDto {
    private int offset;
    private int limit;
    
    /**
     * @param offset offset of search result
     * @param limit page size
     * @return new instance of SearchPageDto
     */
    public static SearchPageDto of(int offset, int limit) {
        SearchPageDto searchPageDto = new SearchPageDto();
        searchPageDto.setOffset(offset);
        searchPageDto.setLimit(limit);
        return searchPageDto;
    }
}
