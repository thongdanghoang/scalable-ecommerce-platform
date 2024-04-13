package vn.id.thongdanghoang.n3tk.common.exceptions;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An exception class that represent the business error for the application.
 */
@Getter
public class N3tkBusinessException extends Exception {
    protected final String field;
    protected final List<BusinessErrorParam> args;

    protected N3tkBusinessException(String field, List<BusinessErrorParam> args) {
        this.field = field;
        this.args = new ArrayList<>(args);
    }

    protected N3tkBusinessException(String field, Map<String, Object> args) {
        this(field, args.entrySet().stream().map(entry -> new BusinessErrorParam(entry.getKey(), entry.getValue())).toList());
    }

    protected N3tkBusinessException(List<BusinessErrorParam> args) {
        this(null, args);
    }
}
