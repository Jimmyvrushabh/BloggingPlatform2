package com.Spring.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Spring.Model.BlogPost;

import com.Spring.Model.Category;
import com.Spring.Model.User;
import com.Spring.Repo.BlogPostRepo;
import com.Spring.Repo.CategoryRepository;
import com.Spring.Repo.Repo;
import com.Spring.Service.BlogPostService;


import DTO.BlogpostDto;
import jakarta.persistence.EntityNotFoundException;



@RestController
@RequestMapping("/blogs")
@CrossOrigin


	public class BlogPostController {

	    @Autowired
	    private BlogPostService blogPostService;
	    
	    @Autowired
	    private BlogPostRepo blogPostRepo;
	    
	    @Autowired
	    private CategoryRepository categoryRepository;
	    
	    @Autowired 
	    private Repo repo;
	    
	    
	    @GetMapping("/test")
	    public String test() {
	    	return "testing";
	    }
	    
	    
	    @GetMapping("/all")
	    public ResponseEntity<List<BlogpostDto>> findAll() {
	        // Fetch all blog posts and convert them into BlogpostDto
	        List<BlogpostDto> blogPostDtos = blogPostService.findAll(); 
	        return ResponseEntity.ok(blogPostDtos); // Return the List of BlogpostDto wrapped in ResponseEntity
	    }

	    @PostMapping("/add")
	    public ResponseEntity<BlogPost> createBlogPost(@RequestBody BlogPost blogPostDTO, Authentication authentication) {
	        // Check if the user is authenticated
	        if (authentication == null || !authentication.isAuthenticated()) {
	            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).build();
	        }

	        // Retrieve the logged-in user
	        String username = authentication.getName();
	        User author = repo.findByUsername(username);

	        // Handle case if the user is not found
	        if (author == null) {
	            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).build();
	        }

	        // Create a new BlogPost object
	        BlogPost blogPost = new BlogPost();
	        blogPost.setTitle(blogPostDTO.getTitle());
	        blogPost.setContent(blogPostDTO.getContent());

	        // Ensure category is valid
	        if (blogPostDTO.getCategory() == null || blogPostDTO.getCategory().getId() == null) {
	            return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(null);  // Return a BadRequest if no category
	        }

	        // Set the category and author for the new blog post
	        blogPost.setCategory(blogPostDTO.getCategory());
	        blogPost.setAuthor(author);

	        // Create and save the blog post
	        BlogPost savedBlogPost = blogPostService.createBlogPost(blogPost);

	        // Return the saved BlogPost object in the response
	        return ResponseEntity.ok(savedBlogPost);
	    }








	   
	    
	    @GetMapping("/paginated")
	    public Page<BlogpostDto> getPaginatedBlogs(
	            @RequestParam(defaultValue = "0") int page, 
	            @RequestParam(defaultValue = "5") int size,
	            @RequestParam(defaultValue = "createdAt") String sortBy,
	            @RequestParam(defaultValue = "desc") String sortDir) {

	        return blogPostService.getPaginatedBlogs(page, size, sortBy, sortDir);
	    }
	    
	
	    @GetMapping("/blogs")
	    public ResponseEntity<List<BlogpostDto>> getAllBlogs() {
	        // Retrieve all blog posts from the repository
	        List<BlogPost> blogPosts = blogPostRepo.findAll();
	        
	        // Convert each BlogPost entity to a BlogpostDto using the service
	        List<BlogpostDto> blogpostDtos = blogPosts.stream()
	                                                  .map(blogPostService::convertToDTO)  // Correct way to call the method from the service
	                                                  .collect(Collectors.toList());
	        
	        // Return the list of BlogpostDto objects
	        return ResponseEntity.ok(blogpostDtos);
	    }




	    

	    @GetMapping("/all1")
	    public ResponseEntity<Page<BlogpostDto>> getAllPosts(@RequestParam int page, @RequestParam int size) {
	        Page<BlogPost> posts = blogPostService.getAllPosts(page, size);

	        // Use lambda to map BlogPost -> BlogPostDTO
	        Page<BlogpostDto> dtoPage = posts.map(post -> new BlogpostDto(post));

	        return ResponseEntity.ok(dtoPage);
	    }


	    @GetMapping("/{id}")
	    public ResponseEntity<BlogpostDto> getPostById(@PathVariable Long id) {
	        // Retrieve the blog post by ID
	        Optional<BlogPost> blogPost = blogPostService.getPostById(id);

	        // If the post exists, convert it to BlogpostDto and return. Otherwise, return 404 NOT FOUND response.
	        return blogPost
	            .map(post -> ResponseEntity.ok(blogPostService.convertToDTO(post)))  // Convert and return 200 OK
	            .orElseGet(() -> ResponseEntity.status(HttpStatus.SC_NOT_FOUND).build());  // Return 404 if not found
	    }

	    @GetMapping("/search")
	    public ResponseEntity<List<BlogPost>> searchByTitle(@RequestParam String keyword) {
	        return ResponseEntity.ok(blogPostService.getTitlesByKeyword(keyword));

	    }


	    
	    
	    @GetMapping("/category/{categoryId}")
	    public ResponseEntity<List<BlogpostDto>> getBlogsByCategory(@PathVariable Long categoryId) {
	        // Retrieve the list of BlogPosts by category ID
	        List<BlogPost> blogs = blogPostRepo.findByCategoryId(categoryId);

	        // Convert each BlogPost to BlogpostDto
	        List<BlogpostDto> blogpostDtos = blogs.stream()
	                                              .map(blogPost -> blogPostService.convertToDTO(blogPost))
	                                              .collect(Collectors.toList());

	        // Return the list of BlogpostDto objects
	        return ResponseEntity.ok(blogpostDtos);
	    }

	    
	  

	    @DeleteMapping("/{id}")
	    public ResponseEntity<String> deletePost(@PathVariable Long id) {
	        blogPostService.deletePost(id);
	        return ResponseEntity.ok("Blog post deleted successfully!");
	    }
	    
	    @PutMapping("/{id}")
	    public ResponseEntity<BlogpostDto> updateBlogPost(@PathVariable Long id, @RequestBody BlogpostDto blogPostDTO) {
	        Optional<BlogPost> existingPostOpt = blogPostRepo.findById(id);
	        
	        if (!existingPostOpt.isPresent()) {
	            return ResponseEntity.notFound().build();
	        }

	        BlogPost existingPost = existingPostOpt.get();
	        
	        // Update the fields
	        existingPost.setTitle(blogPostDTO.getTitle());
	        existingPost.setContent(blogPostDTO.getContent());
	        
	        // Updating the category (if provided)
	        if (blogPostDTO.getCategoryId() != null) {
	            Category category = categoryRepository.findById(blogPostDTO.getCategoryId()).orElseThrow();
	                               
	            existingPost.setCategory(category);
	        }

	        // Save updated blog post
	        BlogPost updatedPost = blogPostRepo.save(existingPost);
	        
	        // Convert and return the updated post as DTO
	        return ResponseEntity.ok(blogPostService.convertToDTO(updatedPost));
	    }


	    
	  
	    @GetMapping("/my-blogs")
	    public ResponseEntity<List<BlogPost>> getMyBlogs(@RequestParam Long userId) {
	        List<BlogPost> blogs = blogPostService.getBlogsByUserId(userId);
	        return ResponseEntity.ok(blogs);
	    }
	    
}
	


