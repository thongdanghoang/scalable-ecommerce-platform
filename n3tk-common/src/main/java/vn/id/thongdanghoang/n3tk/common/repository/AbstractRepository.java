package vn.id.thongdanghoang.n3tk.common.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.id.thongdanghoang.n3tk.common.entities.AbstractAuditableEntity;
import vn.id.thongdanghoang.n3tk.common.utils.JpaUtil;

@RequiredArgsConstructor
public abstract class AbstractRepository<T extends AbstractAuditableEntity> implements JpaRepository<T, Long> {

    private final JpaUtil jpaUtil;

    public T getById(Long id) throws EntityNotFoundException {
        return findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public T getByIdForUpdate(Long id) throws EntityNotFoundException {
        T entity = getById(id);
        detach(entity);
        return entity;
    }

    public void detach(T entity) {
        getEntityManager().detach(entity);
    }

    public T merge(T entity) {
        return getEntityManager().merge(entity);
    }

    public void saveOrUpdate(T entity) {
        if (entity.getId() == null) {
            getEntityManager().persist(entity);
        } else {
            merge(entity);
        }
    }

    public EntityManager getEntityManager() {
        return jpaUtil.getEntityManager();
    }

}
