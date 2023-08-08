package ru.practicum.stats.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatDto;
import ru.practicum.stats.service.EndpointHitService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = StatsController.class)
class StatsControllerTest {

      /*  @Autowired
        ObjectMapper mapper;

        @MockBean
        EndpointHitService service;

        @Autowired
        private MockMvc mvc;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        EndpointHitDto endpointHitDtoSave;
        EndpointHitDto endpointHitDto1;

        @BeforeEach
        void setUp() {

            endpointHitDtoSave = new EndpointHitDto(
                    null,
                    "ewm-main-service",
                    "/events",
                    "121.0.0.1",
                    LocalDateTime.parse("2023-07-22 10:46:15", formatter)
            );

            endpointHitDto1 = new EndpointHitDto(
                    1L,
                    "ewm-main-service",
                    "/events/5",
                    "121.0.0.1",
                    LocalDateTime.parse("2023-07-22 10:46:15", formatter)
            );

            EndpointHitDto endpointHitDto2 = new EndpointHitDto(
                    1L,
                    "ewm-main-service",
                    "/events/5",
                    "121.0.0.1",
                    LocalDateTime.parse("2023-07-22 10:46:15", formatter)
            );

            EndpointHitDto endpointHitDto3 = new EndpointHitDto(
                    1L,
                    "ewm-main-service",
                    "/events/1",
                    "121.0.0.1",
                    LocalDateTime.parse("2023-08-22 10:46:15", formatter)
            );

            EndpointHitDto endpointHitDto4 = new EndpointHitDto(
                    1L,
                    "ewm-main-service",
                    "/events/2",
                    "121.0.0.1",
                    LocalDateTime.parse("2023-07-25 10:46:15", formatter)
            );

            EndpointHitDto endpointHitDto5 = new EndpointHitDto(
                    1L,
                    "ewm-main-service",
                    "/events/1",
                    "121.0.0.1",
                    LocalDateTime.parse("2023-07-22 10:46:15", formatter)
            );

            EndpointHitDto endpointHitDto6 = new EndpointHitDto(
                    1L,
                    "ewm-main-service",
                    "/events/2",
                    "121.0.0.1",
                    LocalDateTime.parse("2023-07-22 10:46:15", formatter)
            );

            EndpointHitDto endpointHitDto7 = new EndpointHitDto(
                    1L,
                    "ewm-main-service",
                    "/events/2",
                    "121.0.0.1",
                    LocalDateTime.parse("2023-07-22 10:46:15", formatter)
            );

        }

        @Test
        void getStatisticsVisits_AllParameters_UniqueFalse_Test() throws Exception {
            List<String> uris = List.of("/events/1", "/events/2", "/events/5");
            String start = "2020-05-05 00:00:00";
            String end = "2035-05-05 00:00:00";
            Boolean unique = false;

            List<ViewStatDto> viewStatDtoList = new ArrayList<>(List.of(
                    new ViewStatDto(
                            "ewm-main-service",
                            "/events/2",
                            3L
                    ),
                    new ViewStatDto(
                            "ewm-main-service",
                            "/events/1",
                            2L
                    ),
                    new ViewStatDto(
                            "ewm-main-service",
                            "/events/5",
                            1L
                    )
            ));

            when(service.findAllViewStats(any(), any(), any(), any()))
                    .thenReturn(viewStatDtoList);

            mvc.perform(get("/stats?start={stat}}&end={end}&uris={uris}&unique={unique}", start, end, uris, unique)
                            .content(mapper.writeValueAsString(viewStatDtoList))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(viewStatDtoList)))
            ;

            List<ViewStatDto> viewStatDtoListTest = service.findAllViewStats(uris, start, end, unique);

            assertEquals(viewStatDtoList, viewStatDtoListTest);
        }

        @Test
        void getStatisticsVisits_WithoutUnique_Test() throws Exception {
            List<String> uris = List.of("/events/1", "/events/2", "/events/5");
            String start = "2020-05-05 00:00:00";
            String end = "2035-05-05 00:00:00";
            Boolean unique = null;

            List<ViewStatDto> viewStatDtoList = new ArrayList<>(List.of(
                    new ViewStatDto(
                            "ewm-main-service",
                            "/events/2",
                            3L
                    ),
                    new ViewStatDto(
                            "ewm-main-service",
                            "/events/1",
                            2L
                    ),
                    new ViewStatDto(
                            "ewm-main-service",
                            "/events/5",
                            1L
                    )
            ));

            when(service.findAllViewStats(any(), any(), any(), any()))
                    .thenReturn(viewStatDtoList);

            mvc.perform(get("/stats?start={stat}}&end={end}&uris={uris}", start, end, uris)
                            .content(mapper.writeValueAsString(viewStatDtoList))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(viewStatDtoList)))
            ;

            List<ViewStatDto> viewStatDtoListTest = service.findAllViewStats(uris, start, end, unique);

            assertEquals(viewStatDtoList, viewStatDtoListTest);
        }

        @Test
        void getStatisticsVisits_WithoutUniqueAndUris_Test() throws Exception {
            List<String> uris = null;
            String start = "2020-05-05 00:00:00";
            String end = "2035-05-05 00:00:00";
            Boolean unique = null;

            List<ViewStatDto> viewStatDtoList = new ArrayList<>(List.of(
                    new ViewStatDto(
                            "ewm-main-service",
                            "/events/2",
                            3L
                    ),
                    new ViewStatDto(
                            "ewm-main-service",
                            "/events/1",
                            2L
                    ),
                    new ViewStatDto(
                            "ewm-main-service",
                            "/events/5",
                            1L
                    )
            ));

            when(service.findAllViewStats(any(), any(), any(), any()))
                    .thenReturn(viewStatDtoList);

            mvc.perform(get("/stats?start={stat}}&end={end}", start, end)
                            .content(mapper.writeValueAsString(viewStatDtoList))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(viewStatDtoList)))
            ;

            List<ViewStatDto> viewStatDtoListTest = service.findAllViewStats(uris, start, end, unique);

            assertEquals(viewStatDtoList, viewStatDtoListTest);
        }

        @Test
        void saveStatisticsTest() throws Exception {
            when(service.save(any()))
                    .thenReturn(endpointHitDto1);

            mvc.perform(post("/hit")
                            .content(mapper.writeValueAsString(endpointHitDtoSave))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))

                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(endpointHitDto1.getId()), Long.class))
                    .andExpect(jsonPath("$.app", is(endpointHitDto1.getApp())))
                    .andExpect(jsonPath("$.uri", is(endpointHitDto1.getUri())))
                    .andExpect(jsonPath("$.ip", is(endpointHitDto1.getIp())))
                    .andExpect(jsonPath("$.timestamp", is(endpointHitDto1.getTimestamp().format(formatter))));

            Mockito.verify(service, Mockito.times(1))
                    .save(endpointHitDtoSave);

            Mockito.verifyNoMoreInteractions(service);
        }*/
    }
