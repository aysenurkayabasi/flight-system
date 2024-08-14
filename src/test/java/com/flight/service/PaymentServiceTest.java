package com.flight.service;

import com.flight.constant.SeatStatus;
import com.flight.entity.Flight;
import com.flight.entity.Seat;
import com.flight.repository.PaymentRepository;
import com.iyzipay.model.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private SeatService seatService;

    @Mock
    private FlightService flightService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private IyzicoSandboxPaymentService iyzicoSandboxPaymentService;


    @Test
    public void testPriceConsistencyBetweenSeatAndPaymentRequest() throws Exception {
        Long seatId = 1L;
        Flight flight = new Flight();
        flight.setId(1L);

        Seat seat = new Seat("1", SeatStatus.AVAILABLE, new BigDecimal(100), flight);
        Seat updatedSeat = new Seat("1", SeatStatus.SOLD, new BigDecimal(100), flight);

        when(seatService.getSeat(seatId)).thenReturn(seat);
        when(flightService.getFlight(seat.getFlight().getId())).thenReturn(flight);

        when(iyzicoSandboxPaymentService.createPaymentRequest(any(BigDecimal.class)))
                .thenReturn(Status.SUCCESS.getValue());

        when(seatService.updateSeatStatus(seat, SeatStatus.SOLD)).thenReturn(updatedSeat);

        ArgumentCaptor<BigDecimal> paymentPriceCaptor = ArgumentCaptor.forClass(BigDecimal.class);

        paymentService.createPayment(seatId);

        verify(iyzicoSandboxPaymentService).createPaymentRequest(paymentPriceCaptor.capture());

        BigDecimal capturedPrice = paymentPriceCaptor.getValue();

        assertEquals(seat.getSeatPrice(), capturedPrice);

        verify(seatService).updateSeatStatus(seat, SeatStatus.SOLD);
    }
}
