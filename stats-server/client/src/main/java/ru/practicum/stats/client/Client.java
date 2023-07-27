package ru.practicum.stats.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.EndpointHitDto;

import java.util.List;

@Slf4j
@Service
public class Client extends BaseClient {
    private static final String API_PREFIX_POST = "/hit";
    private static final String API_PREFIX_GET = "/stats";

    @Autowired
    public Client(@Value("${statistics-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder.build()
        );
    }

    public ResponseEntity<Object> getStatistics(String start, String end, List<String> uris, Boolean unique) {
        String path = API_PREFIX_GET +
                "?start" + start + "?end" + end;
        if (uris != null) {
            path += "&uris=" + uris;
        }
        if (unique != null) {
            path += "&unique=" + unique;
        }
        log.info("GET response from Stat-Server path = {}", path);
        return get(path);
    }

    public ResponseEntity<Object> createStatistics(EndpointHitDto requestDto) {
        log.info("POST response from Stat-Server for create hit = {}", requestDto);
        return post(API_PREFIX_POST, requestDto);
    }
}
