package jdbc.management;

import java.util.List;

public class CinemaDTO {
    private String location;
    private int capacity;
    private List<String> amenities;
    
    public CinemaDTO(String location, int capacity, List<String> amenities) {
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

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    
}
