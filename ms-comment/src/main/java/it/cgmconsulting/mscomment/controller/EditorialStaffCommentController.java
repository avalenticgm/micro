package it.cgmconsulting.mscomment.controller;

import it.cgmconsulting.mscomment.payload.request.EditorialStaffCommentRequest;
import it.cgmconsulting.mscomment.service.EditorialStaffCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EditorialStaffCommentController {

    private final EditorialStaffCommentService escService;

    @PostMapping("/v5")
    public ResponseEntity<?> createEditorialStaffComment(@RequestBody @Valid EditorialStaffCommentRequest request){
        return escService.createEditorialStaffComment(request);
    }
}
