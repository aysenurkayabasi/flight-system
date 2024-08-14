package com.flight.service;

import com.flight.dto.FlightRequestDto;
import com.flight.dto.FlightResponseDto;
import com.flight.entity.Flight;
import com.flight.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class FlightUnitTest {
    @Mock
    private FlightRepository flightRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private FlightService flightService;

    private FlightRequestDto flightRequestDto;
    private Flight flight;
    private FlightResponseDto flightResponseDto;

    @BeforeEach
    void setUp() {
        flightRequestDto = FlightRequestDto.builder().name("f1").description("d1").build();
        flight = Flight.builder().name("f1").description("d1").build();
        flightResponseDto = FlightResponseDto.builder().name("f1").description("d1").build();
    }

    @Test
    void testAddFlight() {
        when(flightRepository.save(any(Flight.class))).thenReturn(flight);
        when(modelMapper.map(any(FlightRequestDto.class), eq(Flight.class))).thenReturn(flight);
        when(modelMapper.map(any(Flight.class), eq(FlightResponseDto.class))).thenReturn(flightResponseDto);
        FlightResponseDto result = flightService.createFlight(flightRequestDto);
        assertEquals(flightResponseDto.getName(), result.getName());
        assertEquals(flightResponseDto.getDescription(), result.getDescription());
        verify(flightRepository).save(any(Flight.class));

    }

    @Test
    void testQueryFlight() {
        Long flightId = 1L;
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(modelMapper.map(any(Flight.class), eq(FlightResponseDto.class))).thenReturn(flightResponseDto);
        FlightResponseDto result = flightService.queryFlight(flightId);
        assertEquals(flightResponseDto.getName(), result.getName());
        assertEquals(flightResponseDto.getDescription(), result.getDescription());
        verify(flightRepository).findById(flightId);
    }

    @Test
    void testFindAllFlights() {
        List<Flight> flights = new ArrayList<>();
        flights.add(Flight.builder().name("f1").build());
        flights.add(Flight.builder().name("f2").build());
        List<FlightResponseDto> flightResponseDtos = new ArrayList<>();
        flightResponseDtos.add(FlightResponseDto.builder().name("f1").build());
        flightResponseDtos.add(FlightResponseDto.builder().name("f2").build());
        when(flightRepository.findAll()).thenReturn(flights);
        when(modelMapper.map(any(Flight.class), eq(FlightResponseDto.class)))
                .thenReturn(flightResponseDtos.get(0), flightResponseDtos.get(1));
        List<FlightResponseDto> result = flightService.findAllFlights();
        assertEquals(flightResponseDtos.size(), result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(flightResponseDtos.get(i).getName(), result.get(i).getName());
            assertEquals(flightResponseDtos.get(i).getDescription(), result.get(i).getDescription());
        }
        verify(flightRepository).findAll();
    }

    @Test
    void testUpdateFlight() {
        Long flightId = 1L;
        FlightRequestDto flightRequestDto = FlightRequestDto.builder().name("Updated Name").description("Updated Desc").build();
        Flight updatedFlight = Flight.builder().name("Updated Name").description("Updated Desc").build();
        FlightResponseDto flightUpdateResponseDto = FlightResponseDto.builder().name("Updated Name").description("Updated Desc").build();
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(modelMapper.map(any(Flight.class), eq(FlightResponseDto.class))).thenReturn(flightUpdateResponseDto);
        when(flightRepository.save(flight)).thenReturn(updatedFlight);
        FlightResponseDto result = flightService.updateFlight(flightId, flightRequestDto);
        assertEquals(updatedFlight.getName(), result.getName());
        assertEquals(updatedFlight.getDescription(), result.getDescription());
        verify(flightRepository).save(any(Flight.class));
    }

    @Test
    void testRemoveFlight() {
        Long flightId = 1L;
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        flightService.deleteFlight(flightId);
        verify(flightRepository).findById(flightId);
        verify(flightRepository).delete((any(Flight.class)));
    }

    @Test
    void testGetFlight() {
        Long flightId = 1L;
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        Flight result = flightService.getFlight(flightId);
        assertEquals(flight.getName(), result.getName());
        assertEquals(flight.getDescription(), result.getDescription());
        verify(flightRepository).findById(flightId);
    }
}
