package jdbc;

import java.awt.image.BufferedImage;
import java.sql.Date;


public class MovieDTO {
	
	private String title;
	private Date releaseDate;
	private BufferedImage poster;
	private String genre;
	private String ageRating;
	
	public MovieDTO(String title, Date releaseDate) {
		super();
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

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getAgeRating() {
		return ageRating;
	}

	public void setAgeRating(String ageRating) {
		this.ageRating = ageRating;
	}


}