package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = "users")
@Data
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "user_name", nullable = false)
    private String name;
    @Column(name = "user_email", length = 512, nullable = false, unique = true)
    private String email;

    User() {
    }

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }


}
