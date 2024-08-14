package com.flight.handler;

import com.flight.exception.BaseException;
import com.flight.handler.response.ErrorModel;
import com.flight.handler.response.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler({BaseException.class})
    public ResponseEntity<?> handleBaseException(HttpServletRequest req, BaseException ex) {
        String message = messageSource.getMessage(ex.getErrorCode().getCode(), null, req.getLocale());
      //  return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(message);
        return new ResponseEntity<>(GenericResponse.builder().error(ErrorModel.builder().code(ex.getErrorCode().getCode()).message(message).build()).success(Boolean.FALSE).build(),ex.getErrorCode().getHttpStatus());
    }


}
