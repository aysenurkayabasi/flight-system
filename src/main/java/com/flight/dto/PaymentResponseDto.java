package com.flight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class PaymentResponseDto {

  private Long flightId;
  private Long seatId;
  private String seatNumber;
  private String paymentStatus;

}
