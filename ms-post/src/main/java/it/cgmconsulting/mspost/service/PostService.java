package it.cgmconsulting.mspost.service;

import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.exception.ResourceNotFoundException;
import it.cgmconsulting.mspost.payload.request.PostRequest;
import it.cgmconsulting.mspost.payload.response.PostDetailResponse;
import it.cgmconsulting.mspost.payload.response.SectionResponse;
import it.cgmconsulting.mspost.repository.PostRepository;
import it.cgmconsulting.mspost.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    @Value("${application.security.internalToken}")
    String internalToken;

    private final PostRepository postRepository;
    private final SectionRepository sectionRepository;

    public ResponseEntity<?> createPost(PostRequest request, int author) {
        Post post = new Post(request.getTitle(), request.getPostImage(), author);
        return ResponseEntity.status(201).body(postRepository.save(post));
    }

    public Post findById(int id){
        return postRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));
    }

    public ResponseEntity<?> getPostDetail(int id) {
        // recuperare il post se esistente e pubblicato
        PostDetailResponse p = postRepository.getPostDetail(id, LocalDate.now())
                .orElseThrow(()-> new ResourceNotFoundException("Post", "id", id));

        // recuperare le sezioni relative al post in questione
        Set<SectionResponse> sections = sectionRepository.getSectionsResponse(id);
        p.setSections(sections);

        // recuperare lo username dell'autore
        String username = getUsername(postRepository.getAuthorId(id)).getBody();
        p.setAuthor(username);
        return ResponseEntity.ok(p);
    }

    private ResponseEntity<String> getUsername(int userId){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization-Internal", internalToken);

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        String url = "http://localhost:9090/ms-auth/v99/"+userId;

        ResponseEntity<String> banner = null;
        try{
            banner = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        } catch (RestClientException e){
            log.error(e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
        return banner;
    }
}
