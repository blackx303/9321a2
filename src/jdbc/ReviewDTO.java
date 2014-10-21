package jdbc;

import java.sql.Date;

public class ReviewDTO {
	
	private String title;
	private Date releaseDate;
	private String username;
	private Integer rating;
	private String review_text;
	
	public ReviewDTO(String title, Date releaseDate, String username,
			Integer rating, String review_text) {
		this.title = title;
		this.releaseDate = releaseDate;
		this.username = username;
		this.rating = rating;
		this.review_text = review_text;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public String getReview_text() {
		return review_text;
	}
	public void setReview_text(String review_text) {
		this.review_text = review_text;
	}
	
}