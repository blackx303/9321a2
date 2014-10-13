package jdbc.users;

public interface UserDAO {
    public void storeUser(UserDTO user);
    public void storeViewer(ViewerDTO user);
    
    public UserDTO findUser(String username);

    String createPending(String username, String saltString, String hashString,
            String email);
    
    public boolean confirmPending(String username, String confirmationKey);

    public PendingUserDTO findPendingUser(String username);
    
    public void confirmPendingUser(String username);
    public ViewerDTO findNormalUser(String username);
    public UserDTO findAdminUser(String username);
}
