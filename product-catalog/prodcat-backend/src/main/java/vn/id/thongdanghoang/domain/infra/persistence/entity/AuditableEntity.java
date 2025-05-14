package vn.id.thongdanghoang.domain.infra.persistence.entity;

import vn.id.thongdanghoang.domain.utils.GeneratedUuidV7;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.*;
import lombok.experimental.FieldNameConstants;

@Setter
@Getter
@FieldNameConstants
@EqualsAndHashCode
@MappedSuperclass
public abstract class AuditableEntity implements Serializable {

  @Id
  @GeneratedUuidV7
  private UUID id;

  @Version
  private Long version;

  @Column
  private String createdBy;

  @Column
  private OffsetDateTime createdDate;

  @Column
  private String lastModifiedBy;

  @Column
  private OffsetDateTime lastModifiedDate;

}
