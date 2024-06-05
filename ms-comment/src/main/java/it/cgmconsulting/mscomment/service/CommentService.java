package it.cgmconsulting.mscomment.service;

import it.cgmconsulting.mscomment.configuration.BeanManagement;
import it.cgmconsulting.mscomment.entity.Comment;
import it.cgmconsulting.mscomment.exception.GenericException;
import it.cgmconsulting.mscomment.exception.ResourceNotFoundException;
import it.cgmconsulting.mscomment.payload.request.CommentRequest;
import it.cgmconsulting.mscomment.payload.response.CommentResponse;
import it.cgmconsulting.mscomment.repository.CommentRepository;
import it.cgmconsulting.mscomment.utils.Consts;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final Map<String,String> getMembers;
    private final BeanManagement bean;

    @Value("${application.security.internalToken}")
    String internalToken;


    public ResponseEntity<?> createComment(CommentRequest request, int author) {
        checkPost(request.getPost());
        Comment comment = new Comment(request.getComment(), author, request.getPost());
        commentRepository.save(comment);
        bean.getMembers(); // aggiorno la mappatura nel caso un nuovo member scrivesse un commento
        return ResponseEntity.status(HttpStatus.CREATED).body("Comment written");
    }

    private ResponseEntity<Boolean> existsPost(int postId){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization-Internal", internalToken);

        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        String url = Consts.GATEWAY + "/" + Consts.MS_POST + "/v99/" + postId;

        ResponseEntity<Boolean> response = null;
        try{
            response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Boolean.class);
        } catch (RestClientException e){
            log.error(e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
        return response;
    }

    public ResponseEntity<?> getComment(int id) {
        CommentResponse c = commentRepository.getComment(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
        c.setAuthor(getMembers.get(String.valueOf(c.getAuthor())));
        return ResponseEntity.status(HttpStatus.OK).body(c);
    }

    public ResponseEntity<?> getCommentBis(int id) {
        Comment comment = findById(id);
        CommentResponse cr = new CommentResponse(comment.getId(),comment.getComment(), getMembers.get(String.valueOf(comment.getAuthor())));
        return ResponseEntity.ok(cr);
    }

    public Comment findById(int id){
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
    }

    public ResponseEntity<?> getComments(int postId){
        checkPost(postId);
        List<CommentResponse> list = commentRepository.getComments(postId);
        for(CommentResponse c : list){
            c.setAuthor(getMembers.get(c.getAuthor()));
        }
        return ResponseEntity.ok(list);
    }

    private void checkPost(int postId){
        if(Boolean.FALSE.equals(existsPost(postId).getBody()))
            throw new ResourceNotFoundException("Post", "id", postId);
    }

    @Transactional
    public ResponseEntity<?> updateComment(int commentId, String comment, int author) {
        Comment c = findById(commentId);
        if(c.getAuthor() != author)
            throw new GenericException("You can only update your comments", HttpStatus.FORBIDDEN);

        if(c.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(1)))
            throw new GenericException("You can only update your comment within 60 seconds from publication", HttpStatus.FORBIDDEN);

        c.setComment(comment);
        c.setUpdatedAt(LocalDateTime.now());
        CommentResponse cr = new CommentResponse(commentId, comment, getMembers.get(String.valueOf(c.getAuthor())));

        return ResponseEntity.ok(cr);

    }

    @Transactional
    public ResponseEntity<?> deleteComment(int commentId, int author) {
        Comment c = findById(commentId);
        if(c.getAuthor() != author)
            throw new GenericException("You can only delete your comment", HttpStatus.FORBIDDEN);

        if(c.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(1)))
            throw new GenericException("You can delete your comment within 60 seconds from publication", HttpStatus.FORBIDDEN);

        commentRepository.delete(c);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

    }

}
