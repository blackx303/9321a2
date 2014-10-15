package jdbc.management;

import java.util.List;

public interface MovieDAO {
    List<MovieDTO> findAll();
}
