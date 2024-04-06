package thongdanghoang.id.vn.n3tkproduct.common.utils;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.springframework.stereotype.Component;

/**
 * Convenient utilities for working with JPA/Hibernate.
 * {@link EntityManager} em in the static and then we can use it.
 */
@Component
@RequiredArgsConstructor
public class JpaUtil {

    private final EntityManager em;

    /**
     * Get the class of an instance or the underlying class
     * of a proxy (without initializing the proxy!). It is
     * almost always better to use the entity name!
     */
    public static Class<?> getPersistenceClassWithoutInitializingProxy(Object entity) {
        if (entity instanceof HibernateProxy proxy) {
            LazyInitializer li = proxy.getHibernateLazyInitializer();
            return li.getPersistentClass();
        } else {
            return entity.getClass();
        }
    }

    public EntityManager getEntityManager() {
        return em;
    }

    /**
     * Remove the given entity from the persistence context
     */
    public void detach(Object object) {
        em.detach(object);
    }
}
