package vn.id.thongdanghoang.n3tk.common.utils;

/**
 * Definition of what can be put into MDC so that later on, can be accessible in its logging context.
 */
public final class MDCContext {

    public static final String CORRELATION_ID_HEADER_NAME = "X-Correlation-Id";
    public static final String CORRELATION_ID = "CorrelationId";
    private MDCContext() {
    }
}
