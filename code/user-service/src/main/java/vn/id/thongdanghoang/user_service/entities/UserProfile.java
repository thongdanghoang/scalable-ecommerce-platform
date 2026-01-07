package vn.id.thongdanghoang.user_service.entities;

import vn.id.thongdanghoang.user_service.entities.generic.AuditableEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfile extends AuditableEntity {

    @NotNull @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
