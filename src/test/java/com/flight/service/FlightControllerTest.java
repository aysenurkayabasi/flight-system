package com.flight.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flight.controller.FlightController;
import com.flight.dto.FlightRequestDto;
import com.flight.dto.FlightResponseDto;
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

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(FlightController.class)
public class FlightControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    @InjectMocks
    private FlightController flightController;

    private FlightRequestDto flightRequestDto;
    private FlightResponseDto flightResponseDto;

    private final Long flightId = 1L;

    @BeforeEach
    void setUp() {
        flightRequestDto = FlightRequestDto.builder().name("f1").description("d1").build();
        flightResponseDto = FlightResponseDto.builder().flightId(1L).name("f1").description("d1").build();
    }

    @Test
    public void testCreateFlight() throws Exception {
        when(flightService.createFlight(any(FlightRequestDto.class))).thenReturn(flightResponseDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/flight")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(flightRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.flightId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.name").value("f1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.description").value("d1"));
    }

    @Test
    public void testUpdateFlight() throws Exception {
        flightResponseDto.setName("f2");
        when(flightService.updateFlight(any(Long.class), any(FlightRequestDto.class))).thenReturn(flightResponseDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/flight/{flightId}", flightId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(flightRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("$.flightId").value(flightId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("f2"));
    }

    @Test
    public void testRemoveFlight() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/flight/{flightId}", flightId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testQueryFlight() throws Exception {
        when(flightService.queryFlight(flightId)).thenReturn(flightResponseDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/flight/{flightId}", flightId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.flightId").value(flightId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("f1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("d1"));
    }

    @Test
    public void testGetAllFlights() throws Exception {
        List<FlightResponseDto> responseDtos = Collections.singletonList(flightResponseDto);
        when(flightService.findAllFlights()).thenReturn(responseDtos);
        mockMvc.perform(MockMvcRequestBuilders.get("/flight"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].flightId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("f1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("d1"));

    }
}
