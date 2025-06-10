package vn.id.thongdanghoang.sep.prodcat.exceptions;

import vn.id.thongdanghoang.sep.prodcat.exceptions.model.BusinessErrorParam;

import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Getter;

/**
 * An exception class that represent the business error for the application.
 */
@Getter
public class BusinessException extends RuntimeException {

  protected final String field;
  protected final String i18nKey;
  protected final ArrayList<BusinessErrorParam> args;
  protected final Response.Status httpStatus;

  public BusinessException(String field, String i18nKey, List<BusinessErrorParam> args) {
    this(field, i18nKey, args, Response.Status.EXPECTATION_FAILED);
  }

  protected BusinessException(String field, String i18nKey, List<BusinessErrorParam> args,
      Response.Status httpStatus) {
    this.field = field;
    this.i18nKey = i18nKey;
    this.args = new ArrayList<>(args);
    this.httpStatus = httpStatus;
  }

  protected BusinessException(String field, String i18nKey, List<BusinessErrorParam> args,
      Response.Status httpStatus, Throwable cause) {
    super(cause);
    this.field = field;
    this.i18nKey = i18nKey;
    this.args = new ArrayList<>(args);
    this.httpStatus = httpStatus;
  }

  protected BusinessException(String field, String i18nKey, Map<String, Object> args) {
    this(field, i18nKey, args.entrySet().stream()
        .map(entry -> new BusinessErrorParam(entry.getKey(), entry.getValue())).toList());
  }

  protected BusinessException(String i18nKey, List<BusinessErrorParam> args) {
    this(null, i18nKey, args);
  }

  protected BusinessException(String i18nKey) {
    this(null, i18nKey, List.of());
  }

  protected BusinessException(String i18nKey, Response.Status httpStatus, Throwable cause) {
    this(null, i18nKey, Collections.emptyList(), httpStatus, cause);
  }
}
