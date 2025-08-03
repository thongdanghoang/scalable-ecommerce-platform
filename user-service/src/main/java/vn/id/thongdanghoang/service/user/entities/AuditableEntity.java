package vn.id.thongdanghoang.service.user.entities;


import vn.id.thongdanghoang.entities.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity extends BaseEntity {

  @NotNull
  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @NotNull
  @Column(name = "last_modified_date", nullable = false)
  @LastModifiedDate
  private LocalDateTime lastModifiedDate;
}
