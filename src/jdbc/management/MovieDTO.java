package jdbc.management;

import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class MovieDTO implements Comparable<MovieDTO> {
	
	private String title;
	private Date releaseDate;
	private BufferedImage poster;
	private List<String> genres;
	private String ageRating;
    private String actors;
    private String synopsis;
    private String director;
	
	public MovieDTO(String title, Date releaseDate, String ageRating, List<String> genres, String director, String actors, String synopsis) {
		super();
		this.title = title;
		this.releaseDate = releaseDate;
        this.ageRating = ageRating;
		this.genres = genres;
		this.director = director;
		this.actors = actors;
		this.synopsis = synopsis;
	}
	
	public MovieDTO(String title, Date releaseDate) {
		this.title = title;
		this.releaseDate = releaseDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public BufferedImage getPoster() {
		return poster;
	}

	public void setPoster(BufferedImage poster) {
		this.poster = poster;
	}

	public List<String> getGenres() {
		return genres;
	}

	public void setGenres(List<String> genre) {
		this.genres = genre;
	}

	public String getAgeRating() {
		return ageRating;
	}

	public void setAgeRating(String ageRating) {
		this.ageRating = ageRating;
	}

    @Override
    public int compareTo(MovieDTO o) {
        return this.releaseDate.compareTo(o.releaseDate);
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    @Override
	public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof MovieDTO)) return false;
        
        MovieDTO m = (MovieDTO) o;
        
        if(! title.equals(m.title)) return false;
        if(! releaseDate.equals(m.releaseDate)) return false;
		
		return true;
	}
    
    public boolean isReleased() {
		java.sql.Date currentDate = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		return releaseDate.before(currentDate);
	}
}