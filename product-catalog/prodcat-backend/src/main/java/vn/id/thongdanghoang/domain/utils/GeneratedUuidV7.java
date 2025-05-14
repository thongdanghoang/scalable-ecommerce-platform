package vn.id.thongdanghoang.domain.utils;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.*;
import org.hibernate.annotations.IdGeneratorType;

@IdGeneratorType(UuidV7Generator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({FIELD, METHOD})
public @interface GeneratedUuidV7 {

}
