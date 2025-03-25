package com.Spring.Service;

import org.springframework.stereotype.Service;

import com.Spring.Model.BlogPost;
import com.Spring.Model.Comments;
import com.Spring.Repo.BlogPostRepo;
import com.Spring.Repo.CommentRepo;

import DTO.CommentDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.Optional;

    @Service
    public class CommentService {

        @Autowired
        private CommentRepo commentRepository;

        @Autowired
        private BlogPostRepo blogPostRepository;

        public Comments addComment(Long blogPostId, Comments comment) {
            Optional<BlogPost> blogPostOptional = blogPostRepository.findById(blogPostId);
            if (blogPostOptional.isPresent()) {
                BlogPost blogPost = blogPostOptional.get();
                comment.setBlogPost(blogPost);
                return commentRepository.save(comment);
            } else {
                throw new RuntimeException("Blog post not found with ID: " + blogPostId);
            }
        }

        public List<Comments> getCommentsByBlogPostId(Long blogPostId) {
            return commentRepository.findByBlogPostId(blogPostId);
        }

        public void deleteComment(Long commentId) {
            commentRepository.deleteById(commentId);
        }
        
        
    

    }
