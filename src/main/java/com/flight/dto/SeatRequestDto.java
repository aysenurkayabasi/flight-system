package com.flight.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Setter
@Getter
@Builder
public class SeatRequestDto {

    @NotNull
    private String seatNumber;

    @NotNull
    private BigDecimal seatPrice;
}
