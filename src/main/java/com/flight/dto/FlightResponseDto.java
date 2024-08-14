package com.flight.dto;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightResponseDto {
    private Long flightId;
    private String name;
    private String description;

}
