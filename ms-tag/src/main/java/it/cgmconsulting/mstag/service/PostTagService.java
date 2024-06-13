package it.cgmconsulting.mstag.service;

import it.cgmconsulting.mstag.entity.PostTag;
import it.cgmconsulting.mstag.entity.PostTagId;
import it.cgmconsulting.mstag.entity.Tag;
import it.cgmconsulting.mstag.repository.PostTagRepository;
import it.cgmconsulting.mstag.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostTagService {

    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;

    public ResponseEntity<?> addTagsToPost(int postId, Set<String> tagNames){
        Set<Tag> tags = tagRepository.getTags(tagNames);
        List<PostTag> postTags = new ArrayList<>();
        for(Tag t : tags){
            postTags.add(new PostTag(new PostTagId(postId, t)));
        }
        postTagRepository.saveAll(postTags);
        return ResponseEntity.status(201).body(postTags);
    }

    @Transactional
    public ResponseEntity<?> updateTagsToPost(int postId, Set<String> tagNames) {

        // se il Set<String> tagNames è vuoto, vengono eliminati tutti i record sulla tabella post_tag
        postTagRepository.deleteByPostId(postId);
        Set<PostTag> newPostTags = new HashSet<>();

        if(!tagNames.isEmpty()) {
            Set<Tag> newTags = tagRepository.getTags(tagNames);
            for (Tag t : newTags) {
                newPostTags.add(new PostTag(new PostTagId(postId, t)));
            }
            postTagRepository.saveAll(newPostTags);
        }
        return ResponseEntity.ok(newPostTags);
    }

    public ResponseEntity<?> getTagsByPost(int postId) {
        Set<String> tags = postTagRepository.getTagsByPost(postId);
        return ResponseEntity.ok(tags);
    }
}
