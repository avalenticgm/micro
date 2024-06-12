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
        Set<Tag> newTags = tagRepository.getTags(tagNames);
        Set<Tag> oldTags = postTagRepository.getTags(postId);

        oldTags.removeIf(t -> !newTags.contains(t));
        oldTags.addAll(newTags);

        Set<PostTag> x = new HashSet<>();
        for(Tag t : oldTags){
            x.add(new PostTag(new PostTagId(postId, t)));
        }
        for(Tag t : oldTags){
            t.setPosts(x);
        }

        return ResponseEntity.ok(x);
    }
}
