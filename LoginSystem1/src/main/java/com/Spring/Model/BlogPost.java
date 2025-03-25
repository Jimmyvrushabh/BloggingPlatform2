package com.Spring.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import DTO.AuthorDTO;

@Entity
@Table(name = "blog_post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class BlogPost {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) 
    @JsonBackReference ("user-blogs")
    private User author;

    private LocalDateTime createdAt = LocalDateTime.now(); 

    @OneToMany(mappedBy = "blogPost", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("blog-comments") // Unique reference name
    private List<Comments> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference("category-blogs") // Unique reference name
    private Category category;
}

