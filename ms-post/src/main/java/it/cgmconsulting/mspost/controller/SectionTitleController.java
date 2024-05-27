package it.cgmconsulting.mspost.controller;

import it.cgmconsulting.mspost.service.SectionTitleService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class SectionTitleController {

    private final SectionTitleService sectionTitleService;


    // POST -> titolo sezione in maiuscolo (ADMIN)
    @PostMapping("/v1/section-titles")
    public ResponseEntity<?> addSectionTitle(@RequestParam @NotBlank @Size(max=50, min=2) String sectionTitle){
        return sectionTitleService.addSectionTitle(sectionTitle);
    }

    // GET ALL -> solo i visible (WRITER)
    @GetMapping("/v2/section-titles")
    public ResponseEntity<?> getAllVisibleSectionTitle(){
        return sectionTitleService.getAllVisibleSectionTitle();
    }

    // GET ALL -> visible e non visible (ADMIN)
    @GetMapping("/v1/section-titles")
    public ResponseEntity<?> getAllSectionTitle(){
        return sectionTitleService.getAllSectionTitle();
    }
}
