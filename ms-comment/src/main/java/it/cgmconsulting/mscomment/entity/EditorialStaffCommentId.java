package it.cgmconsulting.mscomment.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EditorialStaffCommentId {

    @OneToOne
    @JoinColumn(name = "comment_id", nullable = false)
    @EqualsAndHashCode.Include
    private Comment commentId;

}
