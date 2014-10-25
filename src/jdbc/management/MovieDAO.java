package jdbc.management;

import java.util.Date;
import java.util.List;

public interface MovieDAO {
    MoviePosterDTO findMoviePoster(String title, Date releaseDate);
    List<MovieDTO> findAll();

    boolean create(MovieDTO movie, MoviePosterDTO poster);
    List<MovieDTO> findAllNowShowing();
}
