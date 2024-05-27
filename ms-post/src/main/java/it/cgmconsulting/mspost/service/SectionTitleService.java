package it.cgmconsulting.mspost.service;

import it.cgmconsulting.mspost.entity.SectionTitle;
import it.cgmconsulting.mspost.exception.GenericException;
import it.cgmconsulting.mspost.repository.SectionTitleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionTitleService {

    private final SectionTitleRepository sectionTitleRepository;

    public ResponseEntity<?> addSectionTitle(String sectionTitle) {
        sectionTitle = sectionTitle.toUpperCase();
        if(sectionTitleRepository.existsBySectionTitle(sectionTitle))
            throw new GenericException("This section title already exists", HttpStatus.CONFLICT);

        sectionTitleRepository.save(new SectionTitle(sectionTitle));
        return ResponseEntity.status(201).body("Section title created successfully");
    }

    public ResponseEntity<?> getAllVisibleSectionTitle(){
        return ResponseEntity.status(HttpStatus.OK).body(sectionTitleRepository.getAllVisible());
    }

    public ResponseEntity<?> getAllSectionTitle(){
        return ResponseEntity.status(HttpStatus.OK).body(sectionTitleRepository.findAll());
    }
}
