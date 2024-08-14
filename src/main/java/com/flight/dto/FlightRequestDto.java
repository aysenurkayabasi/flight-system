package com.flight.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightRequestDto {

    @NotNull
    private String name;

    @NotNull
    private String description;

}
