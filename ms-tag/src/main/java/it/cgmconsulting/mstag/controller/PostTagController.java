package it.cgmconsulting.mstag.controller;

import it.cgmconsulting.mstag.service.PostTagService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class PostTagController {

    private final PostTagService postTagService;

    // POST per associare i tags ad un post
    @PostMapping("/v2/{postId}")
    public ResponseEntity<?> addTagsToPost(
            @PathVariable @Min(1) int postId,
            @RequestParam Set<String> tagNames){
        return postTagService.addTagsToPost(postId, tagNames);
    }

    // PATCH per modificare i tags associati ad un post
    @PatchMapping("/v2/{postId}")
    public ResponseEntity<?> updateTagsToPost(
            @PathVariable @Min(1) int postId,
            @RequestParam Set<String> tagNames){
        return postTagService.updateTagsToPost(postId, tagNames);
    }


    // get per ottenere i tags associati ad un post
    // ENDPOINT RICHIAMATO DA MS-POST
    @GetMapping("/v99/{postId}")
    public ResponseEntity<?> getTagsByPost(@PathVariable @Min(1) int postId){
        return postTagService.getTagsByPost(postId);
    }
}
