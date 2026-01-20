package vn.id.thongdanghoang.sep.promotion.repository;

import vn.id.thongdanghoang.sep.promotion.entity.Promotion;
import vn.id.thongdanghoang.sep.promotion.entity.Promotion_;

import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class PromotionRepository implements PanacheRepositoryBase<Promotion, UUID> {

    public Uni<Promotion> findByCode(String code) {
        return find(Promotion_.CODE, code).firstResult();
    }

}
