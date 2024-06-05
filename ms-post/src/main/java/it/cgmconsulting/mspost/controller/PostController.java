package it.cgmconsulting.mspost.controller;

import it.cgmconsulting.mspost.payload.request.PostRequest;
import it.cgmconsulting.mspost.service.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final Map<String,String> getWriters;

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

    @PatchMapping("/v1/{id}") // pubblicazione del post
    public ResponseEntity<?> publish(
            @PathVariable @Min(1) int id,
            @RequestParam(required = false) LocalDate publicationDate){
        return postService.publish(id, publicationDate);
    }

    @GetMapping("/v0")
    public ResponseEntity<?> getLastPublishedPost(
            @RequestParam(defaultValue = "0") int pageNumber, // numero di pagina da cui partire
            @RequestParam(defaultValue = "3") int pageSize, // numero di elementi per pagina
            @RequestParam(defaultValue = "publicationDate") String sortBy, // indica la colonna su cui eseguire l'ordinamento
            @RequestParam(defaultValue = "DESC") String direction // indica se l'ordinamento è ASC o DESC
    ){
        return postService.getLastPublishedPost(pageNumber, pageSize, sortBy, direction);
    }
/*
    @PutMapping("/v99/writers")
    public ResponseEntity<?> updateWriters(
            @RequestParam String id,
            @RequestParam String username
    ){
        getWriters.put(id, username);
        return ResponseEntity.ok(null);
    }
*/

    @GetMapping("/v99/{postId}")
    public ResponseEntity<Boolean> existsPost(@PathVariable @Min(1) int postId) {
        return postService.existsById(postId); // Restituisce direttamente un booleano
    }
}
