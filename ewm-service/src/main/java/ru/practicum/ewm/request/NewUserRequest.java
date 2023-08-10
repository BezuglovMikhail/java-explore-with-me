package ru.practicum.ewm.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewUserRequest {
    private String email;
    private String name;
}
