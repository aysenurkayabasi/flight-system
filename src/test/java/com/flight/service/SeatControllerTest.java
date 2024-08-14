package com.flight.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flight.controller.SeatController;
import com.flight.dto.SeatRequestDto;
import com.flight.dto.SeatResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(SeatController.class)
public class SeatControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeatService seatService;

    @InjectMocks
    private SeatController seatController;

    private static SeatRequestDto seatRequestDto;
    private static SeatResponseDto seatResponseDto;

    @BeforeEach
    void setUp() {
        seatRequestDto = SeatRequestDto.builder().seatPrice(BigDecimal.ONE).seatNumber("1").build();
        seatResponseDto = SeatResponseDto.builder().seatId(1L).seatPrice(BigDecimal.ONE).seatNumber("1").build();
    }

    @Test
    public void testAddSeat() throws Exception {
        when(seatService.addSeat(any(Long.class), any(SeatRequestDto.class))).thenReturn(seatResponseDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/flight/1/seat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(seatRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.seatId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.seatPrice").value(BigDecimal.ONE));
    }

    @Test
    public void testUpdateSeat() throws Exception {
        seatResponseDto.setSeatPrice(BigDecimal.TEN);
        when(seatService.updateSeat(any(Long.class), any(SeatRequestDto.class))).thenReturn(seatResponseDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/flight/1/seat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(seatRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("$.seatId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.seatPrice").value(BigDecimal.TEN));
    }


    @Test
    public void testQueryAvailableSeats() throws Exception {
        when(seatService.queryAvailableSeats(1L)).thenReturn(Collections.singletonList(seatResponseDto));
        mockMvc.perform(MockMvcRequestBuilders.get("/flight/1/seat"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].seatId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].seatPrice").value(BigDecimal.ONE));
    }

    @Test
    public void testRemoveSeat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/flight/1/seat"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

}
