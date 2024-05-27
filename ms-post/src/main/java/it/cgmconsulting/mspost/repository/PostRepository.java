package it.cgmconsulting.mspost.repository;

import it.cgmconsulting.mspost.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
