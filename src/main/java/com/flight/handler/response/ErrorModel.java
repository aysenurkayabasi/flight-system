package com.flight.handler.response;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorModel {
    private String code;
    private String message;
}
