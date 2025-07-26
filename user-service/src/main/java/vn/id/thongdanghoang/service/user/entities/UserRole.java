package vn.id.thongdanghoang.service.user.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_roles")
public class UserRole extends AuditableEntity {

  @NotBlank
  @Size(max = 255)
  @Column(name = "name", nullable = false, unique = true)
  private String name;

}
