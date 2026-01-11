package vn.id.thongdanghoang.user_service.entities;

import vn.id.thongdanghoang.user_service.entities.generic.AuditableEntity;

import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {
        @jakarta.persistence.UniqueConstraint(name = "uq_users_provider", columnNames = { "provider_id", "provider_name" })
})
public class User extends AuditableEntity {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_authorities", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "authority_id"))
    @Fetch(FetchMode.SUBSELECT)
    private Set<Authority> authorities = new LinkedHashSet<>();

    @Column(name = "disabled")
    private boolean disabled;

    @NotBlank @Size(max = 255) @Column(name = "provider_id", nullable = false)
    private String providerId;

    @NotBlank @Size(max = 255) @Column(name = "provider_name", nullable = false)
    private String providerName;
}
