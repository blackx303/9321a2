package jdbc.management;

import java.util.Date;

public class MoviePosterDTO {

    private String title;
    private Date releaseDate;
    private String mimeType;
    private byte[] data;

    public MoviePosterDTO(String title, Date releaseDate, String mimetype, byte[] data) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.mimeType = mimetype;
        this.data = data;
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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

}
