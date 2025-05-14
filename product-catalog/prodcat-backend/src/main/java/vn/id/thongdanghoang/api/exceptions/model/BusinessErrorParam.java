package vn.id.thongdanghoang.api.exceptions.model;

import java.io.Serializable;

public record BusinessErrorParam(String key, Object value) implements Serializable {

}
