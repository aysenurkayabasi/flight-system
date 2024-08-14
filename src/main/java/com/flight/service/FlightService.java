package com.flight.service;


import com.flight.constant.ErrorCode;
import com.flight.dto.FlightRequestDto;
import com.flight.dto.FlightResponseDto;
import com.flight.entity.Flight;
import com.flight.exception.FlightException;
import com.flight.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightService {
    private final FlightRepository flightRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public FlightResponseDto createFlight(final FlightRequestDto flightDto) {
        Flight flight = convertToEntity(flightDto);
        flight = flightRepository.save(flight);
        log.info("Flight saved successfully!");
        return convertToDto(flight);
    }

    @Transactional
    public void deleteFlight(final Long flightId) {
        Flight flight = getFlight(flightId);
        flightRepository.delete(flight);
        log.info("Flight deleted successfully!");
    }

    @Transactional(readOnly = true)
    public FlightResponseDto queryFlight(final Long flightId) {
        Flight flight = getFlight(flightId);
        return convertToDto(flight);
    }

    @Transactional
    public FlightResponseDto updateFlight(final Long flightId, final FlightRequestDto flightDto) {
        Flight flight = getFlight(flightId);
        flight.setName(flightDto.getName());
        flight.setDescription(flightDto.getDescription());
        Flight updatedFlight=flightRepository.save(flight);
        log.info("Flight updated successfully!");
        return convertToDto(updatedFlight);
    }

    @Transactional(readOnly = true)
    public List<FlightResponseDto> findAllFlights() {
        return flightRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public Flight getFlight(final Long flightId) {
        return flightRepository.findById(flightId).orElseThrow(() -> {
            throw new FlightException(ErrorCode.FLIGHT_NOT_FOUND);
        });
    }

    private FlightResponseDto convertToDto(Flight entity) {
        return modelMapper.map(entity, FlightResponseDto.class);
    }

    private Flight convertToEntity(FlightRequestDto flightRequestDto) {
        return modelMapper.map(flightRequestDto, Flight.class);
    }

}
