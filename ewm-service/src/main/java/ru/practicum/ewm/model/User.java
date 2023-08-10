package ru.practicum.ewm.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Size(min = 6, max = 254)
    @NotNull
    private String email;

    @Size(min = 2, max = 250)
    @NotNull
    @NotBlank
    private String name;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
