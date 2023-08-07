package ru.practicum.ewm.request;

import lombok.Data;

@Data
public class NewUserRequest {
    private String email;
    private String name;
}
