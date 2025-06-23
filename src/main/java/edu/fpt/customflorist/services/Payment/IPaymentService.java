package edu.fpt.customflorist.services.Payment;

import edu.fpt.customflorist.dtos.Payment.PaymentDTO;
import edu.fpt.customflorist.exceptions.DataNotFoundException;
import edu.fpt.customflorist.models.Enums.PaymentStatus;
import edu.fpt.customflorist.models.Payment;
import edu.fpt.customflorist.responses.Payment.VnpayResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface IPaymentService {
    String createVnPayPayment(HttpServletRequest request, PaymentDTO paymentDTO) throws DataNotFoundException;
    void updatePayment(Long orderId, String statusPayment) throws DataNotFoundException;
    Page<Payment> getAllPayments(Pageable pageable, String status, LocalDateTime fromDate, LocalDateTime toDate, BigDecimal minAmount, BigDecimal maxAmount);

}
