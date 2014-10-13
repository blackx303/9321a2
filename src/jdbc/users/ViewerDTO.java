package jdbc.users;

public class ViewerDTO extends UserDTO {

    private String lastName;
    private String firstName;
    private String nickname;
    private String email;

    public ViewerDTO(String username, String salt, String passwordAndSaltHash,
            String email, String nickname, String firstName, String lastName) {
        super(username, salt, passwordAndSaltHash);
        this.setEmail(email);
        this.setNickname(nickname);
        this.setFirstName(firstName);
        this.setLastName(lastName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
