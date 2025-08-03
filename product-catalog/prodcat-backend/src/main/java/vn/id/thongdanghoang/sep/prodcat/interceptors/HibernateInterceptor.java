package vn.id.thongdanghoang.sep.prodcat.interceptors;

import vn.id.thongdanghoang.sep.prodcat.entity.AuditableEntity;
import vn.id.thongdanghoang.sep.prodcat.entity.AuditableEntity.Fields;

import io.quarkus.hibernate.orm.PersistenceUnitExtension;
import java.time.OffsetDateTime;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.CallbackException;
import org.hibernate.Interceptor;
import org.hibernate.type.Type;

@PersistenceUnitExtension
@Slf4j
public class HibernateInterceptor implements Interceptor {

  private static final String CREATE_EVENT_LOG_NAME = "CREATE";
  private static final String UPDATE_EVENT_LOG_NAME = "UPDATE";
  private static final String DELETE_EVENT_LOG_NAME = "DELETE";

  @Override
  public boolean onPersist(Object entity, Object id, Object[] state, String[] propertyNames,
      Type[] types) throws CallbackException {
    updateAuditInfo(entity, state, propertyNames, CREATE_EVENT_LOG_NAME);
    return false;
  }

  @Override
  public boolean onFlushDirty(Object entity, Object id, Object[] currentState,
      Object[] previousState, String[] propertyNames, Type[] types) {
    updateAuditInfo(entity, currentState, propertyNames, UPDATE_EVENT_LOG_NAME);
    return false;
  }

  @Override
  public void onRemove(Object entity, Object id, Object[] state, String[] propertyNames,
      Type[] types) throws CallbackException {
    updateAuditInfo(entity, state, propertyNames, DELETE_EVENT_LOG_NAME);
  }

  private void logAuditInfo(String eventType, AuditableEntity entity) {
    log.debug("[{}] {} {}", eventType, entity.getClass().getSimpleName(), entity.getId());
  }

  private void updateAuditInfo(Object entity, Object[] state, String[] propertyNames,
      String eventType) {
    if (entity instanceof AuditableEntity auditEntity) {
      var currentUsername = getCurrentUser();
      var now = OffsetDateTime.now();

      if (CREATE_EVENT_LOG_NAME.equals(eventType)) {
        auditEntity.setCreatedBy(currentUsername);
        auditEntity.setCreatedDate(now);
        updateEntityState(state, propertyNames, AuditableEntity.Fields.createdBy, currentUsername);

        updateEntityState(state, propertyNames, Fields.createdDate, now);

      }
      // If current event is created by batch job -> should improve to optimize performance
      auditEntity.setLastModifiedBy(currentUsername);
      updateEntityState(state, propertyNames, AuditableEntity.Fields.lastModifiedBy,
          currentUsername);
      auditEntity.setLastModifiedDate(now);
      updateEntityState(state, propertyNames, Fields.lastModifiedDate, now);
      logAuditInfo(eventType, auditEntity);
    }
  }

  private void updateEntityState(Object[] state, String[] propertyNames, String property,
      Object valueToUpdate) {
    state[Arrays.asList(propertyNames).indexOf(property)] = valueToUpdate;
  }

  private String getCurrentUser() {
    return "unknown";
  }

}