package ru.practicum.ewm.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.stats.client.StatClient;

@Configuration
@RequiredArgsConstructor
public class StatClientConfig {

    private String serverUrl = "http://localhost:9090";

    private String app = "ewm-main";

    private final RestTemplateBuilder builder;

    @Bean
    public StatClient makeStatClient() {
        return new StatClient(
                serverUrl,
                app,
                builder
        );
    }
}