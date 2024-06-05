package it.cgmconsulting.mscomment.service;

import it.cgmconsulting.mscomment.entity.Comment;
import it.cgmconsulting.mscomment.entity.EditorialStaffComment;
import it.cgmconsulting.mscomment.entity.EditorialStaffCommentId;
import it.cgmconsulting.mscomment.payload.request.EditorialStaffCommentRequest;
import it.cgmconsulting.mscomment.payload.response.EditorialStaffCommentResponse;
import it.cgmconsulting.mscomment.repository.EditorialStaffCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
}
