package edu.fpt.customflorist.services.Payment;

import edu.fpt.customflorist.components.RandomStringGenerator;
import edu.fpt.customflorist.configurations.PayOsConfig;
import edu.fpt.customflorist.configurations.VnpayConfig;
import edu.fpt.customflorist.dtos.Payment.PaymentDTO;
import edu.fpt.customflorist.exceptions.DataNotFoundException;
import edu.fpt.customflorist.models.*;
import edu.fpt.customflorist.models.Enums.PaymentMethod;
import edu.fpt.customflorist.models.Enums.PaymentStatus;
import edu.fpt.customflorist.models.Enums.Status;
import edu.fpt.customflorist.repositories.OrderRepository;
import edu.fpt.customflorist.repositories.PaymentRepository;
import edu.fpt.customflorist.repositories.PromotionManagerRepository;
import edu.fpt.customflorist.repositories.UserRepository;
import edu.fpt.customflorist.utils.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.exception.PayOSException;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentData;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService{
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final VnpayConfig vnPayConfig;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final RandomStringGenerator randomStringGenerator;
    private final UserRepository userRepository;
    private final PromotionManagerRepository promotionManagerRepository;
    private final PayOsConfig payOsConfig;

    @Override
    public Page<Payment> getAllPayments(Pageable pageable, String statusStr, LocalDateTime fromDate, LocalDateTime toDate, BigDecimal minAmount, BigDecimal maxAmount) {
        PaymentStatus status = null;
        if (statusStr != null && !statusStr.isEmpty()) {
            try {
                status = PaymentStatus.valueOf(statusStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + statusStr);
            }
        }
        return paymentRepository.findAllWithFilters(status, fromDate, toDate, minAmount, maxAmount, pageable);
    }

    private String generateSignature(long orderCode, int amount, String description, String returnUrl, String cancelUrl, String checksumKey) throws Exception {
        String rawData = String.format("amount=%d&cancelUrl=%s&description=%s&orderCode=%d&returnUrl=%s",
                amount, cancelUrl, description, orderCode, returnUrl);

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(checksumKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] hashBytes = sha256_HMAC.doFinal(rawData.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        String signature = hexString.toString().toLowerCase();
        return signature;
    }

    public CheckoutResponseData createPayOsPayment(HttpServletRequest request, PaymentDTO paymentDTO) throws Exception {
        Long userId = UserUtil.getCurrentUserId();
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new DataNotFoundException("User not found"));
        try {
            Order order = orderRepository.findById(paymentDTO.getOrderId())
                    .orElseThrow(() -> new DataNotFoundException("Order not found"));

            PayOS payOS = new PayOS(
                    payOsConfig.getClientId(),
                    payOsConfig.getApiKey(),
                    payOsConfig.getChecksumKey()
            );

            long orderCode = 100000 + new Random().nextInt(900000);
            int amount = order.getTotalPrice().intValue();
            String description = "Đơn hàng " + orderCode;
            String cancelUrl = payOsConfig.getCancelUrl();
            String returnUrl = payOsConfig.getReturnUrl();
            String checksumKey = payOsConfig.getChecksumKey();

            String signature = generateSignature(orderCode, amount, description, returnUrl, cancelUrl, checksumKey);

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .amount(amount)
                    .description(description)
                    .cancelUrl(cancelUrl)
                    .returnUrl(returnUrl)
                    .signature(signature)
                    .buyerName(user.getName())
                    .buyerEmail(user.getEmail())
                    .buyerPhone(user.getPhone())
                    .buyerAddress(user.getAddress())
                    .expiredAt((System.currentTimeMillis() / 1000 + 10 * 60))
                    .build();

            CheckoutResponseData response;
            try {
                response = payOS.createPaymentLink(paymentData);

            } catch (Exception targetException) {
                if (targetException instanceof PayOSException payOSException) {
                    throw payOSException;
                } else {
                    throw targetException;
                }
            }

            if (!paymentRepository.existsByOrderId(paymentDTO.getOrderId())) {
                Payment payment = new Payment();
                payment.setAmount(order.getTotalPrice());
                payment.setOrder(order);
                payment.setPaymentDate(LocalDateTime.now());
                payment.setStatus(PaymentStatus.PENDING);
                payment.setIsActive(true);
                payment.setPaymentMethod(PaymentMethod.BANK);
                payment.setTransactionCode(String.valueOf(orderCode));
                paymentRepository.save(payment);
            }

            return response;
        } catch (Exception ex) {
            logger.error("Lỗi trong createPayOsPayment: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @Override
    public void updatePayment(Long orderCode, PaymentStatus status) throws DataNotFoundException {
        Payment payment = paymentRepository.findByTransactionCode(String.valueOf(orderCode))
                .orElseThrow(() -> new DataNotFoundException("Payment not found"));

        payment.setStatus(status);
        paymentRepository.save(payment);

    }
}
