package edu.fpt.customflorist.services.Payment;

import edu.fpt.customflorist.dtos.Payment.PaymentDTO;
import edu.fpt.customflorist.exceptions.DataNotFoundException;
import edu.fpt.customflorist.models.Enums.PaymentStatus;
import edu.fpt.customflorist.models.Payment;
import edu.fpt.customflorist.responses.Payment.VnpayResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.payos.type.CheckoutResponseData;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface IPaymentService {
    CheckoutResponseData createPayOsPayment(HttpServletRequest request, PaymentDTO paymentDTO) throws Exception;
    void updatePayment(Long orderCode, PaymentStatus status) throws DataNotFoundException;
    Page<Payment> getAllPayments(Pageable pageable, String status, LocalDateTime fromDate, LocalDateTime toDate, BigDecimal minAmount, BigDecimal maxAmount);

}
