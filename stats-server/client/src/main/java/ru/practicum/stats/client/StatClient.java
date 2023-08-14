package ru.practicum.stats.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
public class StatClient {

    private static final String API_PREFIX_POST = "/hit";
    private static final String API_PREFIX_GET = "/stats";
    private final RestTemplate rest;
    private final String app;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatClient(@Value("${stats-client}") String serverUrl,
                      @Value("${app-name}") String app,
                      RestTemplateBuilder builder) {
        this.app = app;
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public void createStatistics(HttpServletRequest servletRequest) {
        log.debug("Trying send request for 'add'");
        EndpointHitDto dto = new EndpointHitDto(
                null,
                app,
                servletRequest.getRequestURI(),
                servletRequest.getRemoteAddr(),
                LocalDateTime.now()
        );
        HttpEntity<EndpointHitDto> request = new HttpEntity<>(dto, defaultHeaders());
        try {
            rest.exchange(API_PREFIX_POST, HttpMethod.POST, request, Object.class);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException(String.format(" StatusCode: %d, ResponseBody: %s",
                    e.getStatusCode().value(), e.getResponseBodyAsString()));
        }
    }

    public List<ViewStatDto> getStatistics(LocalDateTime start,
                                       LocalDateTime end,
                                       List<String> uris,
                                       Boolean unique) {
        log.debug("Trying send request for 'find'");
        Map<String, Object> parameters = Map.of(
                "start", start.format(formatter),
                "end", end.format(formatter),
                "uris", uris != null ? String.join(",", uris) : "",
                "unique", unique != null ? unique : "false"
        );

        HttpEntity<Object> httpEntity = new HttpEntity<>(defaultHeaders());
        String path = API_PREFIX_GET + "?start={start}&end={end}&uris={uris}&unique={unique}";
        ResponseEntity<List<ViewStatDto>> viewStatDtoList = rest.exchange(
                path,
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<List<ViewStatDto>>() {},
                parameters);

        return viewStatDtoList.getBody();
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<Object> prepareResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }
}
