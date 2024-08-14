package com.flight.controller;


import com.flight.service.PaymentService;
import com.flight.dto.PaymentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/flight")
public class PaymentController {

    private final PaymentService iyzicoPaymentService;

    @PostMapping("/{seatId}/payment")
    public ResponseEntity<PaymentResponseDto> createPayment(@PathVariable final Long seatId) {
        PaymentResponseDto paymentResponseDto = iyzicoPaymentService.createPayment(seatId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(paymentResponseDto);
    }
}
