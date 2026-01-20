package vn.id.thongdanghoang.sep.promotion.repository;

import vn.id.thongdanghoang.sep.promotion.entity.PromotionRedemption;
import vn.id.thongdanghoang.sep.promotion.entity.PromotionRedemption_;

import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class PromotionRedemptionRepository implements PanacheRepositoryBase<PromotionRedemption, UUID> {

    public Uni<PromotionRedemption> findByTransactionId(String transactionId) {
        return find(PromotionRedemption_.TRANSACTION_ID, transactionId).firstResult();
    }

}
