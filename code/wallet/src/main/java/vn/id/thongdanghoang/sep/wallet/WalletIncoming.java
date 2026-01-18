package vn.id.thongdanghoang.sep.wallet;

import vn.id.thongdanghoang.sep.schemas.PromotionApplied;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class WalletIncoming {

    @Incoming("promotion-in")
    @Transactional
    public void processTransaction(PromotionApplied event) {
        // 1. Simulate Check Balance in DB
        // WalletEntity wallet = WalletEntity.findByUserId(event.userId());

        log.info("<< [WALLET] Charging User: {}", event.getUserId());
        log.info("   Original: {}", event.getOriginalAmount());
        log.info("   Discount: {}", event.getDiscountAmount());
        log.info("   ==> FINAL CHARGE: {}", event.getFinalAmount());

        // 2. Simulate Save Transaction History
        // TransactionLog.persist(new TransactionLog(event));

        log.info("   [WALLET] DONE Transaction {}", event.getTransactionId() + "\n");
    }
}
