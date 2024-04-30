package vn.id.thongdanghoang.n3tk.common.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DateRangeDto {
    private LocalDate from;
    private LocalDate to;
}

