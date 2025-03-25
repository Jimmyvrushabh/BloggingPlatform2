package com.Spring.Controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Spring.Model.AuthProvider;
import com.Spring.Model.BlogPost;
import com.Spring.Model.Comments;

import com.Spring.Model.Role;
import com.Spring.Model.User;
import com.Spring.Repo.Repo;
import com.Spring.Service.JwtUtil;
import com.Spring.Service.UserService;

import DTO.LoginRequest;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins   = "http://localhost:3000")
public class UserController {
	
	@Autowired
	private UserService ser;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private Repo repo;
	
	
	
	@GetMapping("/test")
	public String Hello() {
		return "hello";
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
	    if (ser.existsByUsername(user.getUsername())) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Username already exists."));
	    }

	    if (ser.existsByEmail(user.getEmail())) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Email already exists."));
	    }

	    user.setRole(Role.USER); 
	    user.setProvider(AuthProvider.LOCAL);
	    // Set default role as USER
	    ser.reg(user);

	    return ResponseEntity.ok(Map.of("message", "Registration successful"));
	}


	  
	 
	@PostMapping("/log")
	public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
	    User user = ser.getUserByUsername(loginRequest.getUsername());

	    if (user == null || !ser.authenticate(loginRequest.getUsername(), loginRequest.getPassword())) {
	        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
	    }

	    String token = jwtUtil.generateToken(user.getUsername()); 

	    Map<String, Object> response = new HashMap<>();
	    response.put("token", token);
	    response.put("user", Map.of(
	        "id", user.getId(),
	        "username", user.getUsername(),
	        "email", user.getEmail()
	    ));

	    System.out.println("Generated Token: " + token);  
	    System.out.println("Login API Response: " + response);  

	    return ResponseEntity.ok(response);
	}

     
     @GetMapping("/verify")
     public ResponseEntity<?> verifyToken(@RequestHeader("Authorization") String authHeader) {
         try {
             if (authHeader != null && authHeader.startsWith("Bearer ")) {
                 String token = authHeader.substring(7);
                 String username = jwtUtil.extractUsername(token);
                 boolean isValid = jwtUtil.validateToken(token, username);
                 System.out.println(isValid);

                 if (isValid) {
                     String email = ser.getEmailByUsername(username);
                     Map<String, String> response = new HashMap<>();
                     response.put("username", username);
                     response.put("email", email);
                   
                     
                    System.out.println(username);
                    System.out.println(email);
	
                     
                     return ResponseEntity.ok(response);
                 }
             }
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
         } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token verification failed.");
         }
      
         }
     
     
     
     @GetMapping("/verifyy")
     public ResponseEntity<?> verifyTokenn(@RequestHeader("Authorization") String authHeader) {
         try {
             if (authHeader != null && authHeader.startsWith("Bearer ")) {
                 String token = authHeader.substring(7);
                 String username = jwtUtil.extractUsername(token);
                 boolean isValid = jwtUtil.validateToken(token, username);
                 System.out.println(isValid);

                 if (isValid) {
                     // Fetch user details
                     User user = repo.findByUsername(username);
                     if (user == null) {
                         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
                     }
                        

                     // Get email
                     String email = user.getEmail();

                     // Get user's blog posts
                     List<BlogPost> blogPosts = user.getBlogPosts();
                     List<Comments> comments = user.getComments();

                     // Construct response
                     Map<String, Object> response = new HashMap<>();
                     response.put("username", username);
                     response.put("email", email);
                     response.put("blogPosts", blogPosts);
                     response.put("comments", comments);

                     return ResponseEntity.ok(response);
                 }
             }
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
         } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token verification failed.");
         }

     }
}


