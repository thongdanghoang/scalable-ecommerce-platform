package vn.id.thongdanghoang.n3tk.common.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import vn.id.thongdanghoang.n3tk.common.utils.JpaUtil;

import java.util.Optional;

@Getter
@Setter
@MappedSuperclass
@FieldNameConstants
public abstract class AbstractBaseEntity {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
    @Version
    @Column(nullable = false)
    protected int version;
    
    @Transient
    private boolean transientHashCodeLeaked;
    
    public boolean isPersisted() {
        return Optional.ofNullable(getId()).isPresent();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        /*
         * The following is a solution that works for hibernate lazy loading proxies.
         */
        if (JpaUtil.getPersistenceClassWithoutInitializingProxy(this) != JpaUtil.getPersistenceClassWithoutInitializingProxy(obj)) {
            return false;
        }
        
        /*
         * To check whether the class of the argument is equal (or compatible) to the implementing class before
         * casting it
         * */
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        AbstractBaseEntity other = (AbstractBaseEntity) obj;
        if (isPersisted() && other.isPersisted()) { // both entities are not new
            return getId().equals(other.getId());
        }
        return false;
    }
    
    @Override
    @SuppressWarnings("java:S2676") // In case current entity state is transient => should return a negative number
    public int hashCode() {
        if (!isPersisted()) { // is new or is in transient state.
            transientHashCodeLeaked = true;
            return -super.hashCode();
        }
        
        // because hashcode has just been asked for when the object is in transient state at that time super.hashCode() is returned.
        // Now for consistency, we return the same value.
        if (transientHashCodeLeaked) {
            return -super.hashCode();
        }
        
        // The above mechanism obey the rule: if 2 objects are equal, their hashcode must be same.
        return getId().hashCode();
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + getId() + ")";
    }
}