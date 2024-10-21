package fpt.capstone.iUser.repository;

import fpt.capstone.iUser.model.EmailTemplate;
import fpt.capstone.iUser.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {
    @Query("SELECT e FROM EmailTemplate e WHERE e.users = :user AND e.isDeleted = 0")
    Page<EmailTemplate> findByUsers(@Param("user") Users user, Pageable pageable);
}
