package vn.id.thongdanghoang.sep.prodcat.exceptions.model;

import java.io.Serializable;

public record BusinessErrorParam(String key, Object value) implements Serializable {

}
