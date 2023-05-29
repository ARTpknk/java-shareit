package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "users")
@Data
public class User { //для Базы Данных
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    int id;
    @Column(name = "user_name", length=255, nullable=false)
    String name;
    @Column(name = "user_email", length=512, nullable=false, unique=true)
    String email;

User(){
}

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
