package vn.id.thongdanghoang.sep.wallet;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.smallrye.common.annotation.Blocking;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class WalletIncoming {

    @Incoming("promotion-in")
    @Blocking
    @Transactional
    public void processTransaction(PromotionApplied event) {
        // 1. Simulate Check Balance in DB
        // WalletEntity wallet = WalletEntity.findByUserId(event.userId());

        log.info("<< [WALLET] Charging User: {}", event.userId());
        log.info("   Original: {}", event.originalAmount());
        log.info("   Discount: {}", event.discountAmount());
        log.info("   ==> FINAL CHARGE: {}", event.finalAmount());

        // 2. Simulate Save Transaction History
        // TransactionLog.persist(new TransactionLog(event));

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            log.error("Error processing transaction: {}", e.getMessage(), e);
        }

        log.info("   [WALLET] DONE Transaction {}", event.transactionId() + "\n");
    }
}
