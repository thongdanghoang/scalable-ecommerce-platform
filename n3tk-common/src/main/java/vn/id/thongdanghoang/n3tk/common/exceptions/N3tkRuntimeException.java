package vn.id.thongdanghoang.n3tk.common.exceptions;

import lombok.Getter;

@Getter
public class N3tkRuntimeException extends RuntimeException {
    public N3tkRuntimeException(String message, Throwable e) {
        super(message, e);
    }

    public N3tkRuntimeException(String message) {
        super(message);
    }

    public N3tkRuntimeException(Throwable e) {
        super(e);
    }
}