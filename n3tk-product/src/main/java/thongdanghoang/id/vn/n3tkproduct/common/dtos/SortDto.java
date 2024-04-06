package thongdanghoang.id.vn.n3tkproduct.common.dtos;

import lombok.Data;

/**
 * Sort dto class. It is used for table in UI. Currently, angular material only support 1 level sorting
 */
@Data
public class SortDto {
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    private String colId;
    private String sort;
    
    /**
     * @param field sorting field name. This field name should be existing in entity class
     * @param direction either {@code asc} or {@code desc}
     * @return new instance of {@code SortDto}
     */
    public static SortDto of(String field, String direction) {
        var sortDto = new SortDto();
        sortDto.setSort(direction);
        sortDto.setColId(field);
        return sortDto;
    }
}
