package jdbc.management;

import java.util.List;
import java.util.Set;

public class CinemaDTO {
    private String location;
    private int capacity;
    private Set<String> amenities;
    
    public CinemaDTO(String location, int capacity, Set<String> amenities) {
        this.location = location;
        this.setCapacity(capacity);
        this.setAmenities(amenities);
    }

    public String getLocation() {
        return location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Set<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(Set<String> amenities) {
        this.amenities = amenities;
    }

    
}
