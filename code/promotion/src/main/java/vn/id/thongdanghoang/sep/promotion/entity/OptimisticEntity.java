package vn.id.thongdanghoang.sep.promotion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class OptimisticEntity extends BaseEntity {

    @Version
    @Column(name = "version", nullable = false)
    private int version;

}
