package it.cgmconsulting.mscomment.repository;

import it.cgmconsulting.mscomment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
