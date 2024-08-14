package com.flight.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
  FLIGHT_NOT_FOUND("IYZ01", HttpStatus.NOT_FOUND),
  AVAILABLE_SEAT_NOT_FOUND("IYZ02", HttpStatus.NOT_FOUND),
  SEAT_NOT_FOUND("IYZ03", HttpStatus.NOT_FOUND),
  SEAT_ALREADY_SOLD("IYZ04",  HttpStatus.CONFLICT),
  PAYMENT_FAILED("IYZ05", HttpStatus.PAYMENT_REQUIRED),
  SEAT_OPERATION_FAILED("IYZ06", HttpStatus.INTERNAL_SERVER_ERROR);

  private final String code;
  private final HttpStatus httpStatus;


}
