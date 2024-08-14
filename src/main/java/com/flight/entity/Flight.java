package com.flight.entity;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "FLIGHT")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Flight extends Base {

    @NotNull
    @Column(name = "FLIGHT_NAME", unique = true)
    private String name;

    @Column(name = "FLIGHT_DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "flight", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Seat> seats;


}
