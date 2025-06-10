package vn.id.thongdanghoang.sep.prodcat.entity;

import vn.id.thongdanghoang.sep.prodcat.domain.utils.GeneratedUuidV7;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
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
