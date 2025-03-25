package com.Spring.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-blogs") // Same reference name as in BlogPost
    private List<BlogPost> blogPosts = new ArrayList<>();

    @OneToMany(mappedBy = "commenter", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-comments")
    private List<Comments> comments = new ArrayList();

    @JsonCreator
    public User(@JsonProperty("username") String username, 
                @JsonProperty("email") String email, 
                @JsonProperty("password") String password, 
                @JsonProperty("provider") AuthProvider provider, 
                @JsonProperty("role") Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.role = role;
    }
}
