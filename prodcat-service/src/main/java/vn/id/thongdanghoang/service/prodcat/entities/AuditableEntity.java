package vn.id.thongdanghoang.service.prodcat.entities;

import vn.id.thongdanghoang.entities.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * <pre>{@code
 *   @Column(name = "created_by", nullable = false, updatable = false)
 *   @CreatedBy
 *   private String createdBy;
 *
 *   @Column(name = "last_modified_by", nullable = false)
 *   @LastModifiedBy
 *   private String lastModifiedBy;
 * }</pre>
 */
@Getter
@Setter
@FieldNameConstants
@MappedSuperclass
public abstract class AuditableEntity extends BaseEntity {

  @NotNull
  @Column(name = "created_date", nullable = false, updatable = false)
  private LocalDateTime createdDate;

  @NotNull
  @Column(name = "last_modified_date", nullable = false)
  private LocalDateTime lastModifiedDate;

  @Column(name = "created_by", nullable = false, updatable = false)
  private String createdBy;

  @Column(name = "last_modified_by", nullable = false)
  private String lastModifiedBy;
}
