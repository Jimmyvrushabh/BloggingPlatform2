package com.Spring.Controller;


	
	
	import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
	import org.springframework.web.bind.annotation.*;

import com.Spring.Model.AuthProvider;
import com.Spring.Model.Role;
import com.Spring.Model.User;
import com.Spring.Repo.Repo;
import com.Spring.Service.GoogleAuthService;
import com.Spring.Service.JwtUtil;

import java.util.HashMap;
import java.util.Map;

	@RestController
	@RequestMapping("/auth")
	@CrossOrigin(origins = "http://localhost:3000")      // Allow frontend to call backend
	public class AuthController {
		
		@Autowired
		private JwtUtil jwtUtil;

		  private final GoogleAuthService googleAuthService;
		  
		  private final Repo repo;

		    public AuthController(GoogleAuthService googleAuthService, Repo repo) {
		        this.googleAuthService = googleAuthService;
		        this.repo = repo;
		    }
	 
		    @PostMapping("/google")
		    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> data) {
		        String token = data.get("token");

		        if (token == null || token.isEmpty()) {
		            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is missing");
		        }

		        Map<String, String> userInfo = googleAuthService.verifyGoogleToken(token);
		        if (userInfo == null) {
		            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google token");
		        }

		        String email = userInfo.get("email");
		        String name = userInfo.get("name");

		        User user = repo.findByEmail(email);
		        if (user == null) {
		            user = new User();
		            String username = email.length() >= 7 ? email.substring(0, 7) : email;
		            user.setUsername(username.toLowerCase());
		            user.setEmail(email);
		            user.setPassword(""); // No password needed for Google login
		            user.setProvider(AuthProvider.GOOGLE);
		            user.setRole(Role.USER);
		            repo.save(user);
		            
		            System.out.println(user.getUsername());
		        }

		        // Generate JWT token
		        String jwtToken = jwtUtil.generateToken(user.getUsername());

		        // Send user details and token as JSON response
		        Map<String, Object> response = new HashMap();
		        response.put("token", jwtToken);
		        response.put("username", user.getUsername());
		        response.put("email", user.getEmail());
		        response.put("id", user.getId());

		        return ResponseEntity.ok(response);
		    }

	   

	        @GetMapping("/user")
	        public Map<String, Object> getUserDetails(@AuthenticationPrincipal OAuth2User oAuth2User) {
	            if (oAuth2User != null) {
	                return Map.of(
	                    "name", oAuth2User.getAttribute("name"),
	                    "email", oAuth2User.getAttribute("email"),
	                    "picture", oAuth2User.getAttribute("picture") // Profile picture
	                );
	            }
	     
	            return Map.of("error", "User not authenticated");
	        }
	    }

	
