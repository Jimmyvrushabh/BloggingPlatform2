package com.Spring.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // ✅ Correct import
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.Spring.Model.BlogPost;
import com.Spring.Model.Comments;
import com.Spring.Model.User;
import com.Spring.Repo.BlogPostRepo;
import com.Spring.Repo.Repo;
import com.Spring.Service.CommentService;

import DTO.AuthorDTO;
import DTO.CommentDTO;

@RestController
@RequestMapping("/comments")
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogPostRepo blogPostRepo;

    @Autowired
    private Repo repo;

    @PostMapping("/{blogPostId}")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long blogPostId, 
                                                 @RequestBody CommentDTO commentDTO,
                                                 Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("User is not authenticated!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        System.out.println("Authenticated User: " + username);

        User user = repo.findByUsername(username);

        if (user == null) {
            System.out.println("User not found in database!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Validate if blogPost exists
        BlogPost blogPost = blogPostRepo.findById(blogPostId).orElse(null);
        if (blogPost == null) {
            System.out.println("BlogPost not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Convert DTO to entity
        Comments comment = new Comments();
        comment.setContent(commentDTO.getContent());
        comment.setCommenter(user);
        comment.setBlogPost(blogPost); // Ensure the comment is linked to the blog post

        // Save the comment
        Comments savedComment = commentService.addComment(blogPostId, comment);

        // Convert entity to DTO
        CommentDTO savedCommentDTO = new CommentDTO(
            savedComment.getId(), 
            savedComment.getContent(), 
            savedComment.getCreatedAt(), 
            blogPostId,
            Optional.ofNullable(savedComment.getCommenter())
                .map(u -> new AuthorDTO( u.getUsername()))
                .orElse(null) // Handle null commenter safely
        );

        return ResponseEntity.ok(savedCommentDTO);
    }

    @GetMapping("/{blogPostId}") // ✅ Corrected the path
    public ResponseEntity<List<CommentDTO>> getCommentsByBlogPostId(@PathVariable Long blogPostId) {
        List<Comments> comments = commentService.getCommentsByBlogPostId(blogPostId);

        // Convert entity list to DTO list
        List<CommentDTO> commentDTOs = comments.stream()
            .map(this::convertCommentToDTO) // ✅ Use the method properly
            .collect(Collectors.toList());

        return ResponseEntity.ok(commentDTOs);
    }

    private CommentDTO convertCommentToDTO(Comments comment) {
        return new CommentDTO(
            comment.getId(),
            comment.getContent(),
            comment.getCreatedAt(),
            Optional.ofNullable(comment.getBlogPost()).map(BlogPost::getId).orElse(null),
            Optional.ofNullable(comment.getCommenter())
                .map(user -> new AuthorDTO(user.getUsername()))
                .orElse(null) // Handle null commenter safely
        );
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("Comment deleted successfully!");
    }
}
