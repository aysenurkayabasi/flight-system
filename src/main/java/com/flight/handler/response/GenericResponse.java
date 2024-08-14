package com.flight.handler.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse<T> {
    @Builder.Default
    private Boolean success=Boolean.TRUE;
    private T result;
    private ErrorModel error;
    private PaginationModel pagination;
}
