package vn.id.thongdanghoang.domain.infra.persistence.repository.generic;

import java.time.*;
import java.time.format.*;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.BooleanUtils;

/**
 * This utility class proposes a default casting of values for queries. This enables complex search
 * from frontend on boolean, dates, long, doubles.
 */
@UtilityClass
public class DefaultTypeCaster {

  /* here we cannot use : as separator since this string is passed through as URL query param */
  private static final DateTimeFormatter ISO8601DATETIMEFORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH;mm;ssZ");
  private static final DateTimeFormatter ISO8601DATEFORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd");

  Object cast(String fieldName, Object value) {
    if (value == null) {
      return null;
    }
    if (!(value instanceof String)) {
      return value;
    }
    String trimmedValue = value.toString().trim();
    try {
      if (trimmedValue.contains("T")) {
        return OffsetDateTime.parse(trimmedValue, ISO8601DATETIMEFORMAT);
      } else {
        return LocalDate.parse(trimmedValue, ISO8601DATEFORMAT);
      }
    } catch (DateTimeParseException ex1) {
      if (BooleanUtils.TRUE.equalsIgnoreCase(trimmedValue)
          || BooleanUtils.FALSE.equalsIgnoreCase(trimmedValue)) {
        return Boolean.parseBoolean(trimmedValue);
      }
      try {
        return Long.parseLong(trimmedValue);
      } catch (NumberFormatException e) {
        // ignore
      }
      try {
        return Double.parseDouble(trimmedValue);
      } catch (NumberFormatException e) {
        return trimmedValue;
      }
    }
  }
}
