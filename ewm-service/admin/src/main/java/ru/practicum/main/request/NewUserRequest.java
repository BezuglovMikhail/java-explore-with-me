package ru.practicum.main.request;

import lombok.Data;

@Data
public class NewUserRequest {
    private String email;
    private String name;
}
