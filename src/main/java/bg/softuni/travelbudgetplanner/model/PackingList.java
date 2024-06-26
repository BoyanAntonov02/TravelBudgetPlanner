package bg.softuni.travelbudgetplanner.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "packing_lists")
public class PackingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @OneToMany(mappedBy = "packingList", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PackingItem> items;

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

    public Set<PackingItem> getItems() {
        return items;
    }

    public void setItems(Set<PackingItem> items) {
        this.items = items;
    }
}
