package jdbc.management;

import java.util.List;

public interface CinemaDAO {

    List<CinemaDTO> findAll();

    boolean createCinema(CinemaDTO newCinema);

}
