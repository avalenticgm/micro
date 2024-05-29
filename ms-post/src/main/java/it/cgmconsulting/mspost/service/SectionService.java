package it.cgmconsulting.mspost.service;

import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.entity.Section;
import it.cgmconsulting.mspost.entity.SectionTitle;
import it.cgmconsulting.mspost.exception.GenericException;
import it.cgmconsulting.mspost.exception.ResourceNotFoundException;
import it.cgmconsulting.mspost.payload.request.SectionRequest;
import it.cgmconsulting.mspost.payload.request.SectionUpdateRequest;
import it.cgmconsulting.mspost.payload.response.SectionResponse;
import it.cgmconsulting.mspost.repository.SectionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;
    private final SectionTitleService sectionTitleService;
    private final PostService postService;


    public ResponseEntity<?> createSection(SectionRequest request, int author) {
        Post post = postService.findById(request.getPostId());
        if(post.getAuthor() != author)
            throw new GenericException("You are not the same of post author's", HttpStatus.CONFLICT);
        if(post.getSections().stream().anyMatch(s -> s.getTitle().getId() == request.getSectionTitleId()))
            throw new GenericException("The section type in already present in post", HttpStatus.CONFLICT);

        SectionTitle sectionTitle = sectionTitleService.findById(request.getSectionTitleId());

        Section section = new Section(
                post,
                sectionTitle,
                request.getSubTitle(),
                request.getContent(),
                request.getSectionImage(),
                author
        );
        sectionRepository.save(section);
        return ResponseEntity.status(201).body(SectionResponse.mapToResponse(section));
    }

    @Transactional
    public ResponseEntity<?> updateSection(SectionUpdateRequest request, int author, int id) {
        Section s = findById(id);
        if(s.getAuthor() != author)
            throw new GenericException("You are not the same author of this section", HttpStatus.CONFLICT);
        if(s.getPost().getSections().stream().anyMatch(sec -> (sec.getTitle().getId() == request.getSectionTitleId() && sec.getId() != id)))
            throw new GenericException("The section type in already present in post", HttpStatus.CONFLICT);

        SectionTitle sectionTitle = sectionTitleService.findById(request.getSectionTitleId());

        s.setSectionImage(request.getSectionImage());
        s.setUpdatedAt(LocalDateTime.now());
        s.setContent(request.getContent());
        s.setSubTitle(request.getSubTitle());
        s.setTitle(sectionTitle);

        return ResponseEntity.status(200).body(SectionResponse.mapToResponse(s));
    }

    protected Section findById(int id){
        return sectionRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Section", "id", id));
    }
}
