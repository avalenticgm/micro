package it.cgmconsulting.mscomment.service;

import it.cgmconsulting.mscomment.entity.Comment;
import it.cgmconsulting.mscomment.entity.EditorialStaffComment;
import it.cgmconsulting.mscomment.entity.EditorialStaffCommentId;
import it.cgmconsulting.mscomment.exception.ResourceNotFoundException;
import it.cgmconsulting.mscomment.payload.request.EditorialStaffCommentRequest;
import it.cgmconsulting.mscomment.payload.response.EditorialStaffCommentResponse;
import it.cgmconsulting.mscomment.repository.EditorialStaffCommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EditorialStaffCommentService {

    private final EditorialStaffCommentRepository escRepository;
    private final CommentService commentService;

    public ResponseEntity<?> createEditorialStaffComment(EditorialStaffCommentRequest request) {
        Comment c = commentService.findById(request.getCommentId());
        EditorialStaffComment esc = new EditorialStaffComment(
                new EditorialStaffCommentId(c),
                request.getComment()
        );
        escRepository.save(esc);
        return ResponseEntity.status(201).body(EditorialStaffCommentResponse.fromEntityToResponse(esc));
    }

    public ResponseEntity<?> updateEditorialStaffComment(EditorialStaffCommentRequest request) {
        int result = escRepository.updateEditorialStaffComment(request.getComment(), request.getCommentId(), LocalDateTime.now());
        if(result == 0)
            throw new ResourceNotFoundException("Editorial Staff Comment", "id", request.getCommentId());
        return ResponseEntity.ok("Editorial Staff Comment has been updated");
    }

    @Transactional
    public ResponseEntity<?> updateEditorialStaffCommentBis(EditorialStaffCommentRequest request) {
        EditorialStaffComment esc = escRepository.getById(request.getCommentId())
                .orElseThrow(()-> new ResourceNotFoundException("Editorial Staff Comment", "id", request.getCommentId()));
        esc.setComment(request.getComment());
        esc.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(EditorialStaffCommentResponse.fromEntityToResponse(esc));
    }

    public ResponseEntity<?> deleteEditorialStaffComment(int commentId) {
        int result = escRepository.deleteEditorialStaffCommentJPQL(commentId);
        if (result == 0)
            throw new ResourceNotFoundException("Editorial Staff Comment", "id", commentId);
        return ResponseEntity.ok("Editorial Staff Comment deleted successfully");
    }
}
