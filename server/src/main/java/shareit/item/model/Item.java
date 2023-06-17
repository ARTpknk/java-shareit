package shareit.item.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "items")
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "item_name", nullable = false)
    private String name;
    @Column(name = "item_description", length = 512, nullable = false)
    private String description;
    @Column(name = "is_available")
    private Boolean available;
    @Column(name = "owner_id", nullable = false)
    private int ownerId;
    @Column(name = "request_id")
    private Integer requestId;

    public Item() {
    }

    public Item(int id, String name, String description, Boolean available, int ownerId, int requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.ownerId = ownerId;
        this.requestId = requestId;
    }

    public Item(int id, String name, String description, Boolean available, int ownerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.ownerId = ownerId;
    }
}
