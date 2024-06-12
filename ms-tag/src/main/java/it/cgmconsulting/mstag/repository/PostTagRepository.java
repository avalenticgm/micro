package it.cgmconsulting.mstag.repository;

import it.cgmconsulting.mstag.entity.PostTag;
import it.cgmconsulting.mstag.entity.PostTagId;
import it.cgmconsulting.mstag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface PostTagRepository extends JpaRepository<PostTag, PostTagId> {

    @Query(value="SELECT pt.postTagId.tag FROM PostTag pt WHERE pt.postTagId.postId = :postId")
    Set<Tag> getTags(int postId);
}
