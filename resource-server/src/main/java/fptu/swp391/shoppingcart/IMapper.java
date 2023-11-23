package fptu.swp391.shoppingcart;

import java.util.List;

/**
 * @param <E> Entity
 * @param <D> DTO
 * @author thomas.dang
 */
public interface IMapper<E, D> {
    E toEntity(D d);

    List<E> toEntities(List<D> ds);

    D toDTO(E e);

    List<D> toDTOs(List<E> entities);
}
