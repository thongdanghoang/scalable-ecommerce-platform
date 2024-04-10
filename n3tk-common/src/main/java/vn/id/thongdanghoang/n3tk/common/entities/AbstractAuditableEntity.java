package vn.id.thongdanghoang.n3tk.common.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@FieldNameConstants
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractAuditableEntity extends AbstractBaseEntity {
    @Column(name = "createdBy")
    protected String createdBy;

    @Column(name = "creationDate", nullable = false)
    protected LocalDateTime creationDate;

    @Column(name = "lastModificationDate")
    protected LocalDateTime lastModificationDate;

    @Column(name = "lastModifiedBy")
    protected String lastModifiedBy;
}
