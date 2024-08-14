package com.flight.controller;


import com.flight.service.FlightService;
import com.flight.dto.FlightRequestDto;
import com.flight.dto.FlightResponseDto;
import com.flight.handler.response.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RequestMapping("/flight")
@RestController
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;

    @PostMapping
    public ResponseEntity<GenericResponse<FlightResponseDto>> createFlight(@Valid @RequestBody final FlightRequestDto flight) {
        FlightResponseDto responseDto = flightService.createFlight(flight);
        return new ResponseEntity<>(GenericResponse.<FlightResponseDto>builder().result(responseDto).build(),HttpStatus.OK);
    }

    @PutMapping("/{flightId}")
    public ResponseEntity<FlightResponseDto> updateFlight(@PathVariable final Long flightId,@Valid @RequestBody final FlightRequestDto flightDto) {
        FlightResponseDto responseDto = flightService.updateFlight(flightId,flightDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseDto);
    }

    @DeleteMapping("/{flightId}")
    public ResponseEntity<Void> deleteFlight(@PathVariable final Long flightId) {
        flightService.deleteFlight(flightId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{flightId}")
    public ResponseEntity<FlightResponseDto> queryFlight(@PathVariable final Long flightId) {
        FlightResponseDto flightResponseDto = flightService.queryFlight(flightId);
        return ResponseEntity.ok(flightResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<FlightResponseDto>> getAllFlights() {
        List<FlightResponseDto> flightResponseDto = flightService.findAllFlights();
        return ResponseEntity.ok(flightResponseDto);
    }

}
