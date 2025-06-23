package edu.fpt.customflorist.controllers;

import edu.fpt.customflorist.dtos.Payment.PaymentDTO;
import edu.fpt.customflorist.exceptions.DataNotFoundException;
import edu.fpt.customflorist.models.Enums.PaymentStatus;
import edu.fpt.customflorist.models.Payment;
import edu.fpt.customflorist.responses.Payment.VnpayResponse;
import edu.fpt.customflorist.responses.ResponseObject;
import edu.fpt.customflorist.services.Payment.IPaymentService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/api/v1/payment")
@CrossOrigin(origins = {"*", "http://localhost:3000", "https://yourflorist.vercel.app"})
public class PaymentController {
    private final IPaymentService paymentService;

    @PostMapping("/vn-pay")
    public ResponseEntity<?> pay(
            HttpServletRequest request,
            @Valid @RequestBody PaymentDTO paymentDTO,
            BindingResult result
    ) throws DataNotFoundException {

        String vnpayResponse = paymentService.createVnPayPayment(request, paymentDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Payment page")
                .status(HttpStatus.OK)
                .data(VnpayResponse.builder()
                        .code(paymentDTO.getBankCode())
                        .orderId(paymentDTO.getOrderId().toString())
                        .paymentUrl(vnpayResponse)
                        .build())
                .build());
    }

    @GetMapping("/vn-pay-callback")
    public RedirectView payCallbackHandler(HttpServletRequest request) {
        try{
            String status = request.getParameter("vnp_ResponseCode");
            String vnpOrderInfo = request.getParameter("vnp_OrderInfo");
            Long orderId = Long.parseLong(vnpOrderInfo);

            RedirectView redirectView = new RedirectView();

            if (status.equals("00")) {
                if(orderId != 0){
                    paymentService.updatePayment(orderId, "COMPLETED");

                    redirectView = new RedirectView("/payment-success.html");
                    redirectView.addStaticAttribute("orderId", orderId);
                }
            } else {
                if(orderId != 0 ){
                    paymentService.updatePayment(orderId, "FAILED");

                    redirectView = new RedirectView("/payment-fail.html");
                    redirectView.addStaticAttribute("orderId", orderId);
                }
            }
            return redirectView;
        }catch (Exception e){
            return  new RedirectView("/error.html");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPayments(
            @Parameter(description = "Payment status filter: PENDING, COMPLETED, FAILED")
            @RequestParam(required = false) String status,

            @Parameter(description = "Filter payments from this date (ISO 8601: yyyy-MM-dd'T'HH:mm:ss)", example = "2024-03-01T00:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,

            @Parameter(description = "Filter payments up to this date (ISO 8601: yyyy-MM-dd'T'HH:mm:ss)", example = "2024-03-09T23:59:59")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @Parameter(description = "Sort direction (ASC or DESC), default is ASC", example = "ASC")
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "paymentId"));
            Page<Payment> payments = paymentService.getAllPayments(pageable, status, fromDate, toDate, minAmount, maxAmount);

            return ResponseEntity.ok(ResponseObject.builder()
                    .message("List of payments")
                    .status(HttpStatus.OK)
                    .data(payments)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseObject.builder()
                            .message("Error retrieving payments: " + e.getMessage())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .data(null)
                            .build());
        }
    }

}
