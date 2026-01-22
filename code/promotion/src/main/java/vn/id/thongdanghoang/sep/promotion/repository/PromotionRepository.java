package vn.id.thongdanghoang.sep.promotion.repository;

import vn.id.thongdanghoang.sep.promotion.entity.Promotion;
import vn.id.thongdanghoang.sep.promotion.entity.Promotion_;

import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class PromotionRepository implements PanacheRepositoryBase<Promotion, UUID> {

    public Uni<Promotion> findByCode(String code) {
        return find(Promotion_.CODE, code).firstResult();
    }

    public Uni<Integer> incrementUsageCountIfAvailable(UUID promotionId) {
        return update(
            "UPDATE Promotion p SET p.currentUsageCount = p.currentUsageCount + 1" +
                " WHERE p.id = :id AND p.currentUsageCount < p.totalUsageLimit",
            Parameters.with(Promotion_.ID, promotionId)
        );
    }
}
