package jdbc;

public interface UserDAO {
    public void storeUser(UserDTO user);
    
    public UserDTO findUser(String username);
}
