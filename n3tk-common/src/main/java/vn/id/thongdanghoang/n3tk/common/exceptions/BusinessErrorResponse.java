package vn.id.thongdanghoang.n3tk.common.exceptions;

import java.util.List;

public record BusinessErrorResponse(String correlationId, String field, List<BusinessErrorParam> args)
        implements ServerErrorResponse {

    @Override
    public ServerErrorType getErrorType() {
        return ServerErrorType.BUSINESS;
    }
}
