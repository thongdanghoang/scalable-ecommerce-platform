package vn.id.thongdanghoang.user_service.entities.generic;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

/**
 * <pre>
 * {
 *     &#64;code
 *     &#64;Column(name = "created_by", nullable = false, updatable = false)
 *     &#64;CreatedBy
 *     private String createdBy;
 *
 *     &#64;Column(name = "last_modified_by", nullable = false)
 *     @LastModifiedBy
 *     private String lastModifiedBy;
 * }
 * </pre>
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity extends BaseEntity {

    @NotNull @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @NotNull @Column(name = "last_modified_date", nullable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}
