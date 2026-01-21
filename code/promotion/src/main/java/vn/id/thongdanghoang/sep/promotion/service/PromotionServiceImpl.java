package vn.id.thongdanghoang.sep.promotion.service;

import vn.id.thongdanghoang.sep.promotion.entity.Promotion;
import vn.id.thongdanghoang.sep.promotion.entity.PromotionRedemption;
import vn.id.thongdanghoang.sep.promotion.repository.PromotionRedemptionRepository;
import vn.id.thongdanghoang.sep.promotion.repository.PromotionRepository;
import vn.id.thongdanghoang.sep.promotion.type.DiscountType;
import vn.id.thongdanghoang.sep.promotion.type.RedemptionStatus;
import vn.id.thongdanghoang.sep.schemas.PaymentFailed;
import vn.id.thongdanghoang.sep.schemas.PaymentInitiated;
import vn.id.thongdanghoang.sep.schemas.PromotionApplied;
import vn.id.thongdanghoang.sep.schemas.PromotionProcessStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepo;
    private final PromotionRedemptionRepository redemptionRepo;

    @WithTransaction
    @Override
    public Uni<PromotionApplied> applyPromotion(PaymentInitiated request) {
        var code = request.getVoucherCode();

        if (code == null || code.isBlank()) {
            return Uni.createFrom().item(createResponse(request, BigDecimal.ZERO, PromotionProcessStatus.SKIPPED, null));
        }

        return promotionRepo.findByCode(code)
                .chain(promotion -> {
                    if (promotion == null) {
                        return Uni.createFrom().item(createResponse(
                                request,
                                BigDecimal.ZERO,
                                PromotionProcessStatus.NOT_FOUND,
                                "Code not found"));
                    }

                    if (promotion.getCurrentUsageCount() >= promotion.getTotalUsageLimit()) {
                        return Uni.createFrom().item(createResponse(
                                request,
                                BigDecimal.ZERO,
                                PromotionProcessStatus.OUT_OF_STOCK,
                                "Voucher exhausted"));
                    }

                    var discount = calculateDiscount(promotion, request.getOriginalAmount());

                    promotion.setCurrentUsageCount(promotion.getCurrentUsageCount() + 1);

                    var redemption = PromotionRedemption.builder()
                            .transactionId(request.getTransactionId())
                            .userId(request.getUserId())
                            .promotion(promotion)
                            .originalAmount(request.getOriginalAmount())
                            .discountAmount(discount)
                            .finalAmount(request.getOriginalAmount().subtract(discount))
                            .status(RedemptionStatus.PENDING)
                            .build();

                    return redemptionRepo
                            .persist(redemption)
                            .map(ignore -> createResponse(request, discount, PromotionProcessStatus.APPLIED, null));
                });
    }

    private PromotionApplied createResponse(PaymentInitiated req, BigDecimal discount, PromotionProcessStatus status,
            String reason) {
        return PromotionApplied.newBuilder()
                .setTransactionId(req.getTransactionId())
                .setUserId(req.getUserId())
                .setOriginalAmount(req.getOriginalAmount())
                .setFinalAmount(req.getOriginalAmount().subtract(discount))
                .setDiscountAmount(discount)
                .setStatus(status)
                .setFailureReason(reason)
                .build();
    }

    private BigDecimal calculateDiscount(Promotion p, BigDecimal amount) {
        var discount = p.getDiscountType() == DiscountType.FIXED_AMOUNT
                ? p.getDiscountValue()
                : amount.multiply(p.getDiscountValue().divide(
                        BigDecimal.valueOf(100),
                        4,
                        RoundingMode.HALF_UP));

        if (p.getMaxDiscountAmount() != null && discount.compareTo(p.getMaxDiscountAmount()) > 0) {
            discount = p.getMaxDiscountAmount();
        }

        if (discount.compareTo(BigDecimal.ZERO) < 0) {
            discount = BigDecimal.ZERO;
        }

        if (discount.compareTo(amount) > 0) {
            discount = amount;
        }

        return discount;
    }

    @WithTransaction
    @Override
    public Uni<Void> compensateTransaction(PaymentFailed failureEvent) {
        var transactionId = failureEvent.getTransactionId();
        return redemptionRepo.findByTransactionId(transactionId)
                .chain(redemption -> {
                    if (redemption == null || redemption.getStatus() != RedemptionStatus.PENDING) {
                        return Uni.createFrom().voidItem();
                    }

                    redemption.setStatus(RedemptionStatus.CANCELLED);
                    var promo = redemption.getPromotion();
                    promo.setCurrentUsageCount(Math.max(0, promo.getCurrentUsageCount() - 1));

                    return Uni.combine().all().unis(
                            redemptionRepo.persist(redemption),
                            promotionRepo.persist(promo))
                            .asTuple()
                            .replaceWithVoid();
                })
                .replaceWithVoid();
    }

    @WithTransaction
    @Override
    public Uni<Void> confirmTransaction(String transactionId) {
        return redemptionRepo.findByTransactionId(transactionId)
                .chain(redemption -> {
                    if (redemption == null) {
                        return Uni.createFrom().voidItem();
                    }
                    if (redemption.getStatus() == RedemptionStatus.PENDING) {
                        redemption.setStatus(RedemptionStatus.CONFIRMED);
                        return redemptionRepo.persist(redemption);
                    }
                    return Uni.createFrom().voidItem();
                })
                .replaceWithVoid();
    }
}
