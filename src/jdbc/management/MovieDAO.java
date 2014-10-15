package jdbc.management;

import java.util.List;

public interface MovieDAO {
    List<MovieDTO> findAll();

    boolean create(MovieDTO movie);
}
