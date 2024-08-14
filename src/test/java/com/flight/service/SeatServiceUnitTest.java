package com.flight.service;


import com.flight.constant.ErrorCode;
import com.flight.constant.SeatStatus;
import com.flight.dto.SeatRequestDto;
import com.flight.dto.SeatResponseDto;
import com.flight.entity.Flight;
import com.flight.entity.Seat;
import com.flight.exception.SeatException;
import com.flight.repository.SeatRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SeatServiceUnitTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private FlightService flightService;

    @InjectMocks
    private SeatService seatService;

    private SeatRequestDto seatRequestDto;
    private Seat seat;
    private SeatResponseDto seatResponseDto;
    private Flight flight;

    private Long seatId;

    @BeforeEach
    void setUp() {
        seatRequestDto = SeatRequestDto.builder().seatNumber("1").seatPrice(BigDecimal.ONE).build();
        seat = Seat.builder().seatNumber("1").seatPrice(BigDecimal.ONE).build();
        seatResponseDto = SeatResponseDto.builder().seatNumber("1").seatPrice(BigDecimal.ONE).build();
        flight = new Flight();
        seatId = 1L;
    }

    @Test
    void testAddSeat() {
        Long flightId = 1L;
        when(seatRepository.save(any(Seat.class))).thenReturn(seat);
        when(flightService.getFlight(flightId)).thenReturn(flight);
        when(modelMapper.map(any(SeatRequestDto.class), eq(Seat.class))).thenReturn(seat);
        when(modelMapper.map(any(Seat.class), eq(SeatResponseDto.class))).thenReturn(seatResponseDto);
        SeatResponseDto result = seatService.addSeat(flightId, seatRequestDto);
        assertEquals(seatResponseDto.getSeatNumber(), result.getSeatNumber());
        assertEquals(seatResponseDto.getSeatPrice(), result.getSeatPrice());
        verify(seatRepository).save(any(Seat.class));
    }

    @Test
    void testUpdateSeat() {
        SeatRequestDto seatRequestDto = SeatRequestDto.builder().seatPrice(BigDecimal.TEN).build();
        Seat updatedSeat = Seat.builder().seatPrice(BigDecimal.TEN).build();
        SeatResponseDto seatResponseDto = SeatResponseDto.builder().seatPrice(BigDecimal.TEN).build();
        when(seatRepository.findById(seatId)).thenReturn(Optional.of(seat));
        when(seatRepository.save(any(Seat.class))).thenReturn(updatedSeat);
        when(modelMapper.map(any(Seat.class), eq(SeatResponseDto.class))).thenReturn(seatResponseDto);
        SeatResponseDto responseDto = seatService.updateSeat(seatId, seatRequestDto);
        Assertions.assertNotNull(responseDto);
        assertEquals(seatRequestDto.getSeatPrice(), responseDto.getSeatPrice());
        verify(seatRepository).findById(seatId);
        verify(seatRepository).save(seat);
    }


    @Test
    void testUpdateSeat_SeatNotFound_ExceptionThrown() {
        when(seatRepository.findById(seatId)).thenReturn(Optional.empty());
        SeatException exception = assertThrows(SeatException.class,
                () -> seatService.updateSeat(seatId, seatRequestDto));
        assertEquals(ErrorCode.SEAT_NOT_FOUND, exception.getErrorCode());
        verify(seatRepository, times(1)).findById(seatId);
        verify(seatRepository, never()).save(any(Seat.class));
    }

    @Test
    public void testQueryAvailableSeats() {
        List<Seat> availableSeats = new ArrayList<>();
        availableSeats.add(seat);
        when(flightService.getFlight(1L)).thenReturn(flight);
        when(seatRepository.findSeatsByFlightAndSeatStatus(flight, SeatStatus.AVAILABLE)).thenReturn(availableSeats);
        List<SeatResponseDto> result = seatService.queryAvailableSeats(1L);
        assertEquals(1, result.size());

    }

    @Test
    public void testQueryAvailableSeats_AvailableSeatNotFound_ExceptionThrown() {
        when(flightService.getFlight(1L)).thenReturn(flight);
        when(seatRepository.findSeatsByFlightAndSeatStatus(eq(flight), eq(SeatStatus.AVAILABLE))).thenReturn(new ArrayList<>());
        SeatException exception = assertThrows(SeatException.class, () -> seatService.queryAvailableSeats(1L));
        assertEquals(ErrorCode.AVAILABLE_SEAT_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    public void testRemoveSeat() {
        when(seatRepository.findById(seatId)).thenReturn(java.util.Optional.of(seat));
        seatService.deleteSeat(seatId);
        verify(seatRepository).delete(seat);
    }

    @Test
    public void testUpdateSeatStatus() {
        seat.setSeatStatus(SeatStatus.AVAILABLE);
        SeatStatus newStatus = SeatStatus.SOLD;
        seatService.updateSeatStatus(seat, newStatus);
        assertEquals(newStatus, seat.getSeatStatus());
        verify(seatRepository).save(seat);
    }

    @Test
    public void testUpdateSeatStatus_SeatOperationFailed_ExceptionThrown() {
        seat.setSeatStatus(SeatStatus.SOLD);
        SeatStatus newStatus = SeatStatus.AVAILABLE;
        SeatException exception = assertThrows(SeatException.class, () -> seatService.updateSeatStatus(seat, newStatus));
        assertEquals(ErrorCode.SEAT_OPERATION_FAILED, exception.getErrorCode());
        verify(seatRepository, never()).save(seat);
    }

    @Test
    public void testRemoveSeat_SeatOperationFailed_ExceptionThrown() {
        seat.setSeatStatus(SeatStatus.SOLD);
        when(seatRepository.findById(seatId)).thenReturn(Optional.of(seat));
        SeatException exception = assertThrows(SeatException.class, () -> seatService.deleteSeat(seatId));
        assertEquals(ErrorCode.SEAT_OPERATION_FAILED, exception.getErrorCode());
    }
}
