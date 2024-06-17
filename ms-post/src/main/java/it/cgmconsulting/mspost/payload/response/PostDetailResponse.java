package it.cgmconsulting.mspost.payload.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data @NoArgsConstructor
public class PostDetailResponse {

    // from ms-post (Post)
    private int id;
    private String title;
    private LocalDate publicationDate;
    private String postImage;

    // from ms-post (Section)
    private Set<SectionResponse> sections = new HashSet<>();

    // from ms-auth (User)
    private String author;

    // from ms-tag
    private Set<String> tagNames;

    // from ms-rating
    private double average;

    public PostDetailResponse(int id, String title, LocalDate publicationDate, String postImage) {
        this.id = id;
        this.title = title;
        this.publicationDate = publicationDate;
        this.postImage = postImage;
    }

    public PostDetailResponse(int id, String title, LocalDate publicationDate, String postImage, String author) {
        this.id = id;
        this.title = title;
        this.publicationDate = publicationDate;
        this.postImage = postImage;
        this.author = author;
    }
}
