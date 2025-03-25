package com.Spring.Repo;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.Spring.Model.BlogPost;
import com.Spring.Model.User;

@Repository
@EnableJpaRepositories
public interface BlogPostRepo extends JpaRepository<BlogPost, Long > {
	
	List<BlogPost> findByTitleContainingIgnoreCase(String keyword);
	 //   List<BlogPost> findByCategory(String category);
	
	Page<BlogPost> findAll(Pageable pageable);
	
	  List<BlogPost> findByCategoryId(Long categoryId);
	  
	  List<BlogPost> findByAuthor(User author);
	  
	  
	}


