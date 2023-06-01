package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = false)

    int id;
    @Column(name = "item_name", length = 255, nullable = false)
    String name;
    @Column(name = "item_description", length = 512, nullable = false)
    String description;
    @Column(name = "is_available")
    Boolean available;
    @Column(name = "owner_id", nullable = false)
    int ownerId;

    public Item() {
    }

    public Item(int id, String name, String description, Boolean available, int ownerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.ownerId = ownerId;
    }
}
