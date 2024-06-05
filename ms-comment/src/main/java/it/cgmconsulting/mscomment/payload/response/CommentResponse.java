package it.cgmconsulting.mscomment.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class CommentResponse {

    private int id;
    private String comment;
    private String author;
}
