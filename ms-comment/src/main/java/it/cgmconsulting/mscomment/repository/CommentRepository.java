package it.cgmconsulting.mscomment.repository;

import it.cgmconsulting.mscomment.entity.Comment;
import it.cgmconsulting.mscomment.payload.response.CommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value = "SELECT new it.cgmconsulting.mscomment.payload.response.CommentResponse(" +
            "c.id, " +
            "c.comment, " +
            "CAST(c.author AS string)" +
            ") FROM Comment c " +
            "WHERE c.id = :id")
    Optional<CommentResponse> getComment(int id);

    @Query(value = "SELECT new it.cgmconsulting.mscomment.payload.response.CommentResponse(" +
            "c.id, " +
            "CASE WHEN c.censored=false THEN c.comment ELSE '**********' END, " +
            "CAST(c.author AS string)" +
            ") FROM Comment c " +
            "WHERE c.post = :postId")
    List<CommentResponse> getComments(int postId);

    @Query(value = "SELECT c.author FROM Comment c")
    Set<Integer> getAuthorIds();

}
