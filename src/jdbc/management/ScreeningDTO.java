package jdbc.management;

import java.util.Date;

public class ScreeningDTO {
    private String title;
    private Date releaseDate;
    private String cinema;
    private Date screeningTime;
    
    public ScreeningDTO(String title, Date releaseDate, String cinema, Date screeningTime) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.cinema = cinema;
        this.screeningTime = screeningTime;
    }
    
    public String getTitle() {
        return title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getCinema() {
        return cinema;
    }

    public Date getScreeningTime() {
        return screeningTime;
    }
}
