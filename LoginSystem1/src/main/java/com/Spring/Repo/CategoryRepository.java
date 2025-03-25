package com.Spring.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.Spring.Model.Category;


@Repository
@EnableJpaRepositories
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}


