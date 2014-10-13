package jdbc;

public class PendingUserDTO extends UserDTO {

    private String key;
    private String email;

    public PendingUserDTO(String username, String salt,
            String passwordAndSaltHash, String confirmationKey, String email) {
        super(username, salt, passwordAndSaltHash);
        this.key = confirmationKey;
        this.email = email;
    }
    
    public String getConfirmationKey() {
        return key;
    }

    public String getEmail() {
        return email;
    }

}
