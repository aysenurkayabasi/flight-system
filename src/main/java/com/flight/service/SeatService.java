package com.flight.service;


import com.flight.constant.ErrorCode;
import com.flight.constant.SeatStatus;
import com.flight.dto.SeatRequestDto;
import com.flight.dto.SeatResponseDto;
import com.flight.entity.Flight;
import com.flight.entity.Seat;
import com.flight.exception.SeatException;
import com.flight.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final FlightService flightService;
    private final ModelMapper modelMapper;

    @Transactional
    public SeatResponseDto addSeat(final Long flightId, final SeatRequestDto seatRequestDto) {
        Flight flight = flightService.getFlight(flightId);
        Seat seat = convertToEntity(seatRequestDto);
        seat.setFlight(flight);
        Seat addedSeat = seatRepository.save(seat);
        log.info("Seat saved successfully!");
        return convertToDto(addedSeat);
    }

    @Transactional
    public SeatResponseDto updateSeat(final Long seatId, final SeatRequestDto dto) {
        Seat seat = getSeat(seatId);
        if (SeatStatus.SOLD.equals(seat.getSeatStatus())) {
            log.error("Seat can not be updated since it's Sold for seat number: {}", seat.getSeatNumber());
            throw new SeatException(ErrorCode.SEAT_OPERATION_FAILED);
        }
        seat.setSeatNumber(dto.getSeatNumber());
        seat.setSeatPrice(dto.getSeatPrice());
        Seat updatedSeat = seatRepository.save(seat);
        log.info("Seat updated successfully!");
        return convertToDto(updatedSeat);
    }

    @Transactional(readOnly = true)
    public List<SeatResponseDto> queryAvailableSeats(final Long flightId) {
        Flight flight = flightService.getFlight(flightId);
        List<Seat> availableSeats = getAvailableSeats(flight);
        return convertToDto(availableSeats);
    }

    @Transactional
    public void deleteSeat(final Long seatId) {
        Seat seat = getSeat(seatId);
        if (SeatStatus.SOLD.equals(seat.getSeatStatus())) {
            log.error("Seat can not be removed since it's Sold for seat number: {}", seat.getSeatNumber());
            throw new SeatException(ErrorCode.SEAT_OPERATION_FAILED);
        }
        seatRepository.delete(seat);
        log.info("Seat removed successfully!");
    }


    public Seat getSeat(final Long seatId) {
        return seatRepository.findById(seatId).orElseThrow(() -> {
            throw new SeatException(ErrorCode.SEAT_NOT_FOUND);
        });

    }

    public Seat updateSeatStatus(final Seat seat, final SeatStatus seatStatus) {
        if (SeatStatus.SOLD.equals(seat.getSeatStatus())) {
            log.error("Seat can not be updated since it's Sold for seat number: {}", seat.getSeatNumber());
            throw new SeatException(ErrorCode.SEAT_OPERATION_FAILED);
        }
        seat.setSeatStatus(seatStatus);
        log.info("Seat updated successfully!");
        return seatRepository.save(seat);
    }

    private List<Seat> getAvailableSeats(final Flight flight) {
        List<Seat> availableSeats = seatRepository.findSeatsByFlightAndSeatStatus(flight, SeatStatus.AVAILABLE);
        if (availableSeats.isEmpty()) {
            log.error("No available seat found for flight: {}", flight.getName());
            throw new SeatException(ErrorCode.AVAILABLE_SEAT_NOT_FOUND);
        }
        return availableSeats;
    }

    private List<SeatResponseDto> convertToDto(List<Seat> availableSeats) {
        return availableSeats.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private SeatResponseDto convertToDto(Seat entity) {
        return modelMapper.map(entity, SeatResponseDto.class);
    }

    private Seat convertToEntity(SeatRequestDto seatRequestDto) {
        return modelMapper.map(seatRequestDto, Seat.class);
    }
}
