package it.cgmconsulting.mscomment.repository;

import it.cgmconsulting.mscomment.entity.EditorialStaffComment;
import it.cgmconsulting.mscomment.entity.EditorialStaffCommentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditorialStaffCommentRepository extends JpaRepository<EditorialStaffComment, EditorialStaffCommentId> {
}
