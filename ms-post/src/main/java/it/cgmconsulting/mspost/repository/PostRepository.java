package it.cgmconsulting.mspost.repository;

import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.payload.response.PostDetailResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(value="SELECT new it.cgmconsulting.mspost.payload.response.PostDetailResponse(" +
            "p.id, " +
            "p.title, " +
            "p.publicationDate, " +
            "p.postImage" +
            ") FROM Post p " +
            "WHERE p.id = :id " +
            "AND (p.publicationDate IS NOT NULL AND p.publicationDate <= :now)")
    Optional<PostDetailResponse> getPostDetail(int id, LocalDate now);

    @Query(value="SELECT p.author FROM Post p WHERE p.id = :postId")
    int getAuthorId(int postId);

    Optional<Post> findByIdAndPublicationDateIsNotNullAndPublicationDateLessThanEqual(int id, LocalDate now);
}
