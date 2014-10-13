package jdbc.users;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.xml.bind.DatatypeConverter;

public class UserDTO {
    private String username;
    private String salt;
    private String passwordAndSaltHash;
    
    public UserDTO(String username, String salt, String passwordAndSaltHash) {
        this.username = username;
        this.salt = salt;
        this.passwordAndSaltHash = passwordAndSaltHash;
    }

    public String getUsername() {
        return this.username;
    }
    
    public byte[] getSalt() {
        return DatatypeConverter.parseHexBinary(this.salt);
    }
    
    public void setSalt(byte[] salt) {
        this.salt = DatatypeConverter.printHexBinary(salt);
    }
    
    public void setPassword(String password) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        
        try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(salt);
        
        byte[] hash = digest.digest(password.getBytes("UTF-8"));

        String saltHexString = javax.xml.bind.DatatypeConverter.printHexBinary(salt).toLowerCase();
        String hashHexString = javax.xml.bind.DatatypeConverter.printHexBinary(hash).toLowerCase();
        
        this.salt = saltHexString;
        this.passwordAndSaltHash = hashHexString;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.err.println("App depends upon presence of SHA-256");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.err.println("App requires UTF8 support");
        }
    }
    
    
    public byte[] getPasswordAndSaltHash() {
        return DatatypeConverter.parseHexBinary(this.passwordAndSaltHash);
    }
    
    public void setPasswordAndSaltHash(byte[] hash) {
        this.passwordAndSaltHash = DatatypeConverter.printHexBinary(hash);
    }
}
