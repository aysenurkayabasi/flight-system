package com.flight.service;

import com.flight.constant.ErrorCode;
import com.flight.constant.SeatStatus;
import com.flight.dto.PaymentResponseDto;
import com.flight.entity.Flight;
import com.flight.entity.Payment;
import com.flight.entity.Seat;
import com.flight.exception.PaymentException;
import com.flight.repository.PaymentRepository;
import com.iyzipay.model.Status;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final SeatService seatService;
    private final FlightService flightService;
    private final IyzicoSandboxPaymentService iyzicoSandboxPaymentService;

    @Transactional
    @Synchronized
    public PaymentResponseDto createPayment(final Long seatId) {
        Seat seat = seatService.getSeat(seatId);
        Flight flight = flightService.getFlight(seat.getFlight().getId());
        if (SeatStatus.SOLD.equals(seat.getSeatStatus())) {
            throw new PaymentException(ErrorCode.SEAT_ALREADY_SOLD);
        }
        String paymentStatus = iyzicoSandboxPaymentService.createPaymentRequest(seat.getSeatPrice());
        if (Status.SUCCESS.getValue().equals(paymentStatus)) {
            seat=seatService.updateSeatStatus(seat, SeatStatus.SOLD);
            savePayment(seat);
            log.info("Payment successful for flight: " + flight.getName());
        } else {
            log.error("Payment failed for flight: " + flight.getName());
            throw new PaymentException(ErrorCode.PAYMENT_FAILED);
        }
        return getPaymentResponseDto(seat);
    }

    private PaymentResponseDto getPaymentResponseDto(Seat seat) {
        return PaymentResponseDto.builder().seatId(seat.getId()).seatNumber(seat.getSeatNumber()).flightId(seat.getFlight().getId()).paymentStatus(Status.SUCCESS.getValue()).build();
    }

    private void savePayment(final Seat seat) {
        Payment payment = Payment.builder().price(seat.getSeatPrice()).seat(seat).build();
        paymentRepository.save(payment);
        log.info("Payment saved successfully!");

    }
}
