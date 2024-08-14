package com.flight.controller;


import com.flight.dto.SeatRequestDto;
import com.flight.dto.SeatResponseDto;
import com.flight.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/flight")
public class SeatController {

    private final SeatService seatService;

    @PostMapping("/{flightId}/seat")
    public ResponseEntity<SeatResponseDto> addSeat(@PathVariable final Long flightId,
                                                   @Valid @RequestBody final SeatRequestDto seatDto) {
        SeatResponseDto returnSeatDto=seatService.addSeat(flightId, seatDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnSeatDto);

    }

    @PutMapping("/{seatId}/seat")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<SeatResponseDto> updateSeat(@PathVariable final Long seatId, @Valid @RequestBody final SeatRequestDto seatDto) {
        SeatResponseDto returnSeatDto=seatService.updateSeat(seatId,seatDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(returnSeatDto);

    }

    @DeleteMapping("/{seatId}/seat")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteSeat(@PathVariable final Long seatId) {
        seatService.deleteSeat(seatId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{flightId}/seat")
    public ResponseEntity<List<SeatResponseDto>> queryAvailableSeats(@PathVariable final Long flightId) {
        List<SeatResponseDto> seatDtoList = seatService.queryAvailableSeats(flightId);
        return ResponseEntity.ok(seatDtoList);
    }


}
