package com.Spring.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Optional;

import com.Spring.Model.User;
import com.Spring.Repo.Repo;

@Service
public class UserService  implements UserDetailsService {
	
	@Autowired
	private Repo repo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	   private JwtUtil jwtUtil;
	
	

	public User reg(User user) {
		 user.setPassword(passwordEncoder.encode(user.getPassword()));
		return repo.save(user);
	}
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		 User user = repo.findByUsername(username);
		if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
		
		
		 return new org.springframework.security.core.userdetails.User(

		   user.getUsername(),
           user.getPassword(),
           Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
     
         
         
    }
	
	
	 public String getEmailByUsername(String username) {
	       User user = repo.findByUsername(username);
	        return (user != null) ? user.getEmail() : null;
	    }
	 
	 
 public boolean existsByUsername(String username) {
		    return repo.existsByUsername(username);
		}
	 
	 public boolean existsByEmail(String email) {
		 return repo.existsByEmail(email);
	 }
	 
	  public User findByEmail(String email) {
	        return repo.findByEmail(email);
	    }
	  
	  public void save(User user) {
	        repo.save(user);
	    }
	  
	  
	  public User getUserByUsername(String username) {
		    return repo.findByUsername(username); // Assuming repo is your UserRepository
		}
	  
	  public boolean authenticate(String username, String rawPassword) {
		    User user = repo.findByUsername(username);
		    
		    if (user == null) {
		        return false; // User does not exist
		    }

		    return passwordEncoder.matches(rawPassword, user.getPassword());
		}




	}
	

