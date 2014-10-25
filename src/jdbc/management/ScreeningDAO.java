package jdbc.management;

import java.util.List;

public interface ScreeningDAO {

    List<ScreeningDTO> findAll();

    boolean create(ScreeningDTO screening);

}
