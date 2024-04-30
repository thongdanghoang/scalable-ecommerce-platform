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
    @Column(name = "created_by")
    protected String createdBy;

    @Column(name = "creation_date", nullable = false)
    protected LocalDateTime creationDate;

    @Column(name = "last_modification_date")
    protected LocalDateTime lastModificationDate;

    @Column(name = "last_modified_by")
    protected String lastModifiedBy;
}
