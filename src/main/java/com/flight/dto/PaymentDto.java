package com.flight.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PaymentDto {

    @NotNull
    private Long seatId;

    @NotNull
    private Long flightId;
}
