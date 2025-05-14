package vn.id.thongdanghoang.domain.infra.persistence.mapper;

import org.mapstruct.*;

/**
 * This class defines generic mapping methods between a Model and an Entity. More infos can be found
 * <a
 * href="https://mapstruct.org/documentation/dev/api/org/mapstruct/MappingConstants.ComponentModel.html">
 * in the mapstruct doc</a>.
 */
public interface IMapper<MODEL, ENTITY> {

  /**
   * Converts a mapped DB Entity to a business Model.
   */
  MODEL toModel(ENTITY entity);

  /**
   * Converts a business Model to a mapped DB Entity.
   */
  ENTITY toEntity(MODEL model);

  /**
   * Partially updates a target-mapped DB Entity with values coming from source Entity. Every null
   * field from the source Entity will be ignored. Others are updated into target Entity.
   */
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void partialUpdate(ENTITY source, @MappingTarget ENTITY target);

  /**
   * Fully updates a target-mapped DB Entity with values coming from source Entity. All values are
   * updated into target Entity, that means including null values from the source Entity.
   */
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
  void fullUpdate(ENTITY source, @MappingTarget ENTITY target);
}
