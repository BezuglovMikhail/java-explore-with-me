package ru.practicum.ewm.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.stats.client.StatClient;

@Configuration
@RequiredArgsConstructor
public class StatClientConfig {

    @Value("${stats-client}")
    private String serverUrl;

    @Value("${app-name}")
    private String app;

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