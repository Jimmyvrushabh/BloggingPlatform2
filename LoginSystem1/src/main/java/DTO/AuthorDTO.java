package DTO;

public class AuthorDTO {

    private String username;

    // Constructors
    public AuthorDTO() {}

    public AuthorDTO(String username) {
     
        this.username = username;
    }

    // Getters and Setters
 
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}

