package bg.softuni.travelbudgetplanner.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "packing_lists")
public class PackingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "packingList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PackingItem> items = new ArrayList<>();

    @OneToOne(mappedBy = "packingList")
    private Trip trip;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public List<PackingItem> getItems() {
        return items;
    }

    public void setItems(List<PackingItem> items) {
        this.items = items;
    }
}
