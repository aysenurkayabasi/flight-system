package com.flight.service;

import com.flight.dto.FlightRequestDto;
import com.flight.dto.FlightResponseDto;
import com.flight.entity.Flight;
import com.flight.repository.FlightRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FlightServiceIntegrationTest {

    @Autowired
    private FlightService flightService;

    @Autowired
    private FlightRepository flightRepository;

    @Test
    void addFlightTest() {
        FlightRequestDto flightDto = FlightRequestDto.builder().name("f1").build();
        FlightResponseDto result = flightService.createFlight(flightDto);
        assertNotNull(result.getFlightId());
        Optional<Flight> savedFlight = flightRepository.findById(result.getFlightId());
        assertTrue(savedFlight.isPresent());
        assertEquals(flightDto.getName(), savedFlight.get().getName());
    }

    @Test
    public void testDeleteFlight() {
        Flight flight = Flight.builder().name("f1").build();
        flightRepository.save(flight);
        Long flightId = flight.getId();
        flightService.deleteFlight(flightId);
        assertNull(flightRepository.findById(flightId).orElse(null), "Flight should be deleted");
    }

    @Test
    public void testUpdateFlight() {
        Flight flight = Flight.builder().name("F2").build();
        flightRepository.save(flight);
        Long flightId = flight.getId();
        FlightRequestDto updateDto = FlightRequestDto.builder().name("F3").build();
        FlightResponseDto responseDto = flightService.updateFlight(flightId, updateDto);
        assertNotNull(responseDto, "Flight response should not be null");
        Assertions.assertEquals(updateDto.getName(), responseDto.getName());
        Assertions.assertEquals(updateDto.getDescription(), responseDto.getDescription());
    }

    @Test
    public void testFindAllFlights() {
        Flight flight = Flight.builder().name("F4").build();
        flightRepository.save(flight);
        List<FlightResponseDto> flightDtos = flightService.findAllFlights();
        assertNotNull(flightDtos);
        Assertions.assertNotNull(flightDtos);
    }

    @Test
    public void testQueryFlight() {
        Flight flight = Flight.builder().name("F5").build();
        flightRepository.save(flight);
        Long flightId = flight.getId();
        FlightResponseDto responseDto = flightService.queryFlight(flightId);
        assertNotNull(responseDto, "Flight response should not be null");
        Assertions.assertEquals(flight.getName(), responseDto.getName());
        Assertions.assertEquals(flight.getDescription(), responseDto.getDescription());
    }


}
