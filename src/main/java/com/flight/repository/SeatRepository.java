package com.flight.repository;

import com.flight.constant.SeatStatus;
import com.flight.entity.Flight;
import com.flight.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findSeatsByFlightAndSeatStatus(Flight flight, SeatStatus seatStatus);

    @Lock(value = LockModeType.PESSIMISTIC_READ)
    Optional<Seat> findById(Long id);

}
