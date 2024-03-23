package fptu.swp391.shoppingcart.admin.repository.impl;

import com.querydsl.core.types.dsl.SetPath;
import com.querydsl.jpa.impl.JPAQuery;
import fptu.swp391.shoppingcart.admin.repository.UserRepositoryCustom;
import fptu.swp391.shoppingcart.user.authentication.entity.Authority;
import fptu.swp391.shoppingcart.user.authentication.entity.QAuthority;
import fptu.swp391.shoppingcart.user.authentication.entity.QUserAuthEntity;
import fptu.swp391.shoppingcart.user.authentication.entity.UserAuthEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    @PersistenceContext
    private EntityManager em;


    @Override
    public List<UserAuthEntity> findAllByAuthoritiesNotContainingRoleUser() {
        // user querydsl to find all users' authorities not containing authority ROLE_USER
        SetPath<Authority, QAuthority> authorities = QUserAuthEntity.userAuthEntity.authorities;
        QAuthority authority = QAuthority.authority;
        JPAQuery<UserAuthEntity> query = new JPAQuery<>(em);
        return query
                .from(QUserAuthEntity.userAuthEntity)
                .leftJoin(authorities, authority)
                .where(authority.name.ne("ROLE_USER").or(authority.isNull()))
                .fetch();
    }

    @Override
    public Optional<UserAuthEntity> findByAuthoritiesNotContainingRoleUserAndUsername(String username) {
        // user querydsl to find user's authorities not containing authority ROLE_USER
        QAuthority authority = QAuthority.authority;
        JPAQuery<UserAuthEntity> query = new JPAQuery<>(em);
        return query
                .from(QUserAuthEntity.userAuthEntity)
                .leftJoin(QUserAuthEntity.userAuthEntity.authorities, authority)
                .where(authority.name.ne("ROLE_USER").or(authority.isNull()))
                .where(QUserAuthEntity.userAuthEntity.username.eq(username))
                .fetch().stream().findFirst();
    }
}
