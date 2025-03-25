package com.Spring.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Spring.Model.BlogPost;
import com.Spring.Model.Category;
import com.Spring.Model.Comments;
import com.Spring.Model.User;
import com.Spring.Repo.BlogPostRepo;
import com.Spring.Repo.CategoryRepository;
import com.Spring.Repo.Repo;

import DTO.AuthorDTO;
import DTO.BlogpostDto;
import DTO.CommentDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BlogPostService {
    
    @Autowired
    private BlogPostRepo blogPostRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private Repo repo;

    public BlogPost createBlogPost(BlogPost blogPost) {
        // Check if the category is provided and if the category ID is valid
        if (blogPost.getCategory() == null || blogPost.getCategory().getId() == null) {
            throw new IllegalArgumentException("Category ID is required");
        }

        // Fetch the Category from the database by its ID
        Category category = categoryRepository.findById(blogPost.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category with ID " + blogPost.getCategory().getId() + " not found"));

        // Set the valid category to the BlogPost
        blogPost.setCategory(category);

        // Save the BlogPost
        return blogPostRepository.save(blogPost); // Return the saved BlogPost directly
    }



    
    public List<BlogpostDto> getall() {
    	List<BlogPost> blogPosts = blogPostRepository.findAll(); // Fetch all BlogPost entities
        return blogPosts.stream() // Convert the list of BlogPost entities to BlogpostDto
                        .map(this::convertToDTO) // Convert each BlogPost to BlogpostDto
                        .collect(Collectors.toList()); // Collect into a list of BlogpostDto
    }
 
    	

    public Page<BlogPost> getAllPosts(int page, int size) {
        return blogPostRepository.findAll(PageRequest.of(page, size));
    }

    public Optional<BlogPost> getPostById(Long id) {
        return blogPostRepository.findById(id);
    }

   
    public void deletePost(Long id) {
        blogPostRepository.deleteById(id);
    }
    
    public List<BlogPost> getTitlesByKeyword(String keyword) {
        return blogPostRepository.findByTitleContainingIgnoreCase(keyword);
    }
    
 


   


    //  Delete BlogPost by ID
    public void deletePostt(Long id) {
        if (!blogPostRepository.existsById(id)) {
            throw new RuntimeException("BlogPost not found with id " + id);
        }
        blogPostRepository.deleteById(id);
    }
    

     
    	public Page<BlogpostDto> getPaginatedBlogs(int page, int size, String sortBy, String sortDir) {
    	    Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    	    Pageable pageable = PageRequest.of(page, size, sort);

    
    	    Page<BlogPost> blogPosts = blogPostRepository.findAll(pageable);

    	   
    	    return blogPosts.map(blogPost -> new BlogpostDto(
    	        blogPost.getId(),
    	        blogPost.getTitle(),
    	        blogPost.getContent(),
    	        Optional.ofNullable(blogPost.getAuthor())
    	            .map(author -> new AuthorDTO(author.getUsername()))
    	            .orElse(null), // ✅ Use AuthorDTO for author
    	        blogPost.getComments() != null 
    	            ? blogPost.getComments().stream()
    	                .map(comment -> new CommentDTO(
    	                    comment.getId(),
    	                    comment.getContent(),
    	                    comment.getCreatedAt(),
    	                    comment.getBlogPost() != null ? comment.getBlogPost().getId() : null,
    	                    comment.getCommenter() != null 
    	                        ? new AuthorDTO(comment.getCommenter().getUsername()) 
    	                        : null
    	                ))
    	                .collect(Collectors.toList())
    	            : Collections.emptyList(),
    	        blogPost.getCategory() != null ? blogPost.getCategory().getId() : null,
    	        blogPost.getCreatedAt()
    	    ));
    	}





    public List<BlogpostDto> findAll() {
        List<BlogPost> blogPosts = blogPostRepository.findAll(); // Fetch all BlogPost entities
        return blogPosts.stream() // Convert the list of BlogPost entities to BlogpostDto
                        .map(this::convertToDTO) // Convert each BlogPost to BlogpostDto
                        .collect(Collectors.toList()); // Collect into a list of BlogpostDto
    }
	
	

	public List<BlogPost> getBlogsByUserId(Long userId) {
        User user = repo.findById(userId)
                      .orElseThrow(() -> new RuntimeException("User not found"));
        return blogPostRepository.findByAuthor(user); // ✅ Fetch blogs using author
    }


	private CommentDTO convertCommentToDTO(Comments comment) {
	    return new CommentDTO(
	        comment.getId(),
	        comment.getContent(),
	        comment.getCreatedAt(),
	        Optional.ofNullable(comment.getBlogPost()).map(BlogPost::getId).orElse(null), // ✅ Avoid NullPointerException
	        Optional.ofNullable(comment.getCommenter())
	            .map(user -> new AuthorDTO(user.getUsername())) // ✅ Only provide username for AuthorDTO
	            .orElse(null) // Handle null commenter
	    );
	}


	
	
	public BlogpostDto convertToDTO(BlogPost blogPost) {
	    return new BlogpostDto(
	        blogPost.getId(),
	        blogPost.getTitle(),
	        blogPost.getContent(),
	        
	        // Handle null author and convert to AuthorDTO (only username needed)
	        Optional.ofNullable(blogPost.getAuthor())
	            .map(user -> new AuthorDTO(user.getUsername())) // Convert to `AuthorDTO`
	            .orElse(null), // Handle null author

	        // Convert each comment to CommentDTO (empty list if no comments)
	        Optional.ofNullable(blogPost.getComments())
	            .orElse(Collections.emptyList()) // If no comments, return empty list
	            .stream()
	            .map(this::convertCommentToDTO) // Convert each comment to DTO
	            .collect(Collectors.toList()), // Collect into a list
	        
	        // Handle null category and convert to category ID
	        Optional.ofNullable(blogPost.getCategory())
	            .map(Category::getId) // Extract the ID from category
	            .orElse(null), // Handle null category
	        
	        blogPost.getCreatedAt() // Set the createdAt timestamp
	    );
	}
	
}
