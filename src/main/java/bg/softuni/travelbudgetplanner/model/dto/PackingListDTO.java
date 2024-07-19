package bg.softuni.travelbudgetplanner.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PackingListDTO {

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
