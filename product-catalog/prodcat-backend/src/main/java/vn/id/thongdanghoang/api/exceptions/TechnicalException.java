package vn.id.thongdanghoang.api.exceptions;

import lombok.Getter;

/**
 * An exception class that represents a runtime error for the digiFLUX application.
 */
@Getter
public class TechnicalException extends RuntimeException {

  public TechnicalException(String message, Throwable e) {
    super(message, e);
  }

  public TechnicalException(String message) {
    super(message);
  }

  public TechnicalException(Throwable e) {
    super(e);
  }
}
