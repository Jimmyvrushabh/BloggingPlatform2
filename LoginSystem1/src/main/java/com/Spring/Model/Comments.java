package com.Spring.Model;



import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor



public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-comments")  // Avoid exposing full User object
    private User commenter;

    private String content;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "blog_post_id", nullable = false)
    @JsonBackReference("blog-comments") // Same reference name as in BlogPost
    private BlogPost blogPost;
}

    
    


