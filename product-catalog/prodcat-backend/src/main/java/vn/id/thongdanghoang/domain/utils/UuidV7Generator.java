package vn.id.thongdanghoang.domain.utils;

import java.util.EnumSet;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.*;

public class UuidV7Generator implements BeforeExecutionGenerator {

  @Override
  public Object generate(SharedSessionContractImplementor session, Object owner,
      Object currentValue, EventType eventType) {
    return UuidUtils.randomV7();
  }

  @Override
  public EnumSet<EventType> getEventTypes() {
    return EventTypeSets.INSERT_ONLY;
  }
}
