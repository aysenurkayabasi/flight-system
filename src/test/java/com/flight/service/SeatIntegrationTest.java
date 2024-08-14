package com.flight.service;


import com.flight.constant.SeatStatus;
import com.flight.dto.SeatRequestDto;
import com.flight.dto.SeatResponseDto;
import com.flight.entity.Flight;
import com.flight.entity.Seat;
import com.flight.repository.FlightRepository;
import com.flight.repository.SeatRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SeatIntegrationTest {

    @Autowired
    private FlightService flightService;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private SeatService seatService;

    @Autowired
    private SeatRepository seatRepository;

    @Test
    void testAddSeat() {
        Flight flight = Flight.builder().name("f10").build();
        flightRepository.save(flight);
        Long flightId = flight.getId();
        SeatRequestDto seatRequestDto = SeatRequestDto.builder().seatNumber("s1").seatPrice(BigDecimal.TEN).build();
        SeatResponseDto responseDto = seatService.addSeat(flightId, seatRequestDto);
        assertNotNull(responseDto);
        assertEquals(seatRequestDto.getSeatNumber(), responseDto.getSeatNumber());

    }

    @Test
    public void testUpdateSeat() {
        Flight flight = Flight.builder().name("f12").build();
        flightRepository.save(flight);
        Seat seat = Seat.builder().seatNumber("S2").seatPrice(BigDecimal.ONE).flight(flight).build();
        seatRepository.save(seat);
        Long seatId = seat.getId();
        SeatRequestDto updateDto = SeatRequestDto.builder().seatNumber("S1").seatPrice(BigDecimal.TEN).build();
        SeatResponseDto responseDto = seatService.updateSeat(seatId, updateDto);
        assertNotNull(responseDto, "Seat response should not be null");
        Assertions.assertEquals(updateDto.getSeatNumber(), responseDto.getSeatNumber());
        Assertions.assertEquals(updateDto.getSeatPrice(), responseDto.getSeatPrice());
    }

    @Test
    public void testRemoveSeat() {
        Flight flight = Flight.builder().name("f13").build();
        flightRepository.save(flight);
        Seat seat = Seat.builder().seatNumber("S3").seatPrice(BigDecimal.ONE).flight(flight).build();
        seatRepository.save(seat);
        Long seatId = seat.getId();
        assertDoesNotThrow(() -> seatService.deleteSeat(seatId));
        assertNull(seatRepository.findById(seatId).orElse(null));
    }

    @Test
    public void testQueryAvailableSeats() {
        Flight flight = Flight.builder().name("f14").build();
        flightRepository.save(flight);
        Seat seat = Seat.builder().seatNumber("S4").seatStatus(SeatStatus.AVAILABLE).seatPrice(BigDecimal.ONE).flight(flight).build();
        seatRepository.save(seat);
        List<SeatResponseDto> responseDtos = seatService.queryAvailableSeats(flight.getId());
        assertNotNull(responseDtos);
        Assertions.assertEquals(1, responseDtos.size());

    }

}


