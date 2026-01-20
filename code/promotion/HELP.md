Complex Rollback Scenario:

Promotion Service: Receives request, checks for remaining vouchers -> Deducts 1 voucher from Redis (Inventory: 100 ->
99). -> Sends event to Wallet.

Wallet Service: Receives payment event. Checks database and finds user has insufficient funds -> Transaction fails.

Problem: User doesn't lose money, but vouchers are lost (ghost consumption).

Saga Solution: Wallet Service must send the payment.failed event. Promotion Service must listen to this event to add 1
voucher back to Redis (Inventory: 99 -> 100).

Trong tính toán tài chính, việc thiết lập scale (số chữ số thập phân) phụ thuộc vào giai đoạn tính toán (trung gian hay
kết quả cuối) và loại tiền tệ bạn đang sử dụng.

1. Quy tắc thiết lập Scale tối ưu
   Kết quả cuối cùng (Final Result): Sử dụng scale dựa trên đơn vị tiền tệ nhỏ nhất theo tiêu chuẩn ISO 4217.
   Đa số các loại tiền tệ (USD, EUR, GBP): Scale = 2 (ví dụ: $10.25).
   Tiền tệ không có số thập phân (VND, JPY): Scale = 0 (ví dụ: 10,000đ).
   Một số ngoại lệ (BHD, KWD): Scale = 3.
   Tính toán trung gian (Intermediate Calculations): Nên sử dụng Scale lớn hơn (thường là 4 đến 10) để tránh sai số tích
   lũy do làm tròn quá sớm.
2. Cách tính toán Discount an toàn
   Khi tính toán giá trị giảm giá dựa trên phần trăm, hãy tuân thủ quy trình sau:
3. Lựa chọn RoundingMode phù hợp
   HALF_UP: Phổ biến nhất trong thương mại (0.5 làm tròn lên).
   HALF_EVEN (Banker's Rounding): Thường dùng trong kế toán/ngân hàng để giảm thiểu sai số thống kê khi thực hiện hàng
   triệu phép tính (làm tròn đến số chẵn gần nhất).
4. Các lỗi cần tránh
   Không dùng double: Tuyệt đối không dùng double để lưu trữ phần trăm hay giá tiền vì sẽ gây sai số dấu phẩy động (ví
   dụ 0.1 + 0.2 có thể ra 0.30000000000000004).
   Constructor new BigDecimal(double): Luôn dùng new BigDecimal(String) hoặc BigDecimal.valueOf(double) để khởi tạo giá
   trị chính xác.

```java
BigDecimal originalPrice = new BigDecimal("100.00");
BigDecimal discountPercent = new BigDecimal("15"); // 15%

// 1. Tính toán trung gian (Giảm giá = Giá gốc * (Phần trăm / 100))
// Sử dụng scale lớn để giữ độ chính xác
BigDecimal discountValue = originalPrice.multiply(discountPercent)
        .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);

// 2. Làm tròn kết quả cuối cùng theo đơn vị tiền tệ (ví dụ USD dùng scale 2)
BigDecimal finalDiscount = discountValue.setScale(2, RoundingMode.HALF_UP);
BigDecimal priceAfterDiscount = originalPrice.subtract(finalDiscount);
```