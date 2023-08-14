package ru.practicum.ewm.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class NewUserRequest {

    @Email
    @Size(min = 6, max = 254)
    @NotBlank
    private String email;

    @Size(min = 2, max = 250)
    @NotBlank
    private String name;
}
