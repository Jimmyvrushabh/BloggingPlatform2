package com.Spring.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.Spring.Model.Comments;

import java.util.List;


	
	@Repository
	@EnableJpaRepositories
	public interface CommentRepo extends JpaRepository<Comments, Long> {
	    List<Comments> findByBlogPostId(Long blogPostId);
	}



