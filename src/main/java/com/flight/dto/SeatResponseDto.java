package com.flight.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatResponseDto {
    private Long seatId;

    private String seatNumber;

    private BigDecimal seatPrice;

}
