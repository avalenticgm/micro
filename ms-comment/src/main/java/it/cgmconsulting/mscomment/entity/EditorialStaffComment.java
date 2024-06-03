package it.cgmconsulting.mscomment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EditorialStaffComment {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private EditorialStaffCommentId id;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

}
