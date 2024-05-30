package it.cgmconsulting.mspost.controller;

import it.cgmconsulting.mspost.payload.request.PostRequest;
import it.cgmconsulting.mspost.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/v2")
    public ResponseEntity<?> createPost(
            @RequestBody @Valid PostRequest request,
            @RequestHeader("userId") int author){
        return postService.createPost(request, author);
    }

    @GetMapping("/v0/{id}")
    public ResponseEntity<?> getPostDetail(@PathVariable @Min(1) int id){
        return postService.getPostDetail(id);
    }

    @GetMapping("/v0/bis/{id}")
    public ResponseEntity<?> getPostDetailBis(@PathVariable @Min(1) int id) {
        return postService.getPostDetailBis(id);
    }

    @PatchMapping("/v1/{id}")
    public ResponseEntity<?> publish(
            @PathVariable @Min(1) int id,
            @RequestParam(required = false) LocalDate publicationDate){
        return postService.publish(id, publicationDate);
    }
}
