package it.cgmconsulting.mstag.repository;

import it.cgmconsulting.mstag.entity.PostTag;
import it.cgmconsulting.mstag.entity.PostTagId;
import it.cgmconsulting.mstag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {

    @Query(value="SELECT pt.postTagId.tag FROM PostTag pt WHERE pt.postTagId.postId = :postId")
    Set<Tag> getTags(int postId);

    @Modifying
    @Query("DELETE FROM PostTag pt WHERE pt.postTagId.postId = :postId")
    void deleteByPostId(int postId);

    @Query(value="SELECT pt.postTagId.tag.tagName " +
            "FROM PostTag pt WHERE pt.postTagId.postId = :postId " +
            "ORDER BY pt.postTagId.tag.tagName")
    Set<String> getTagsByPost(int postId);
}
