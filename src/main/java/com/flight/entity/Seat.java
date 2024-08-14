package com.flight.entity;

import com.flight.constant.SeatStatus;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Entity
@Table(name = "SEAT")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Seat extends Base {

    @NotNull
    @Column(name = "SEAT_NUMBER", unique = true)
    private String seatNumber;

    @Column(name = "SEAT_STATUS")
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private SeatStatus seatStatus=SeatStatus.AVAILABLE;

    @NotNull
    @Column(name = "SEAT_PRICE")
    private BigDecimal seatPrice;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "FLIGHT_ID")
    private Flight flight;
}
