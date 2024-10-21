package fpt.capstone.SalesInnovate.iLead.repository;

import fpt.capstone.SalesInnovate.iLead.model.LeadRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadRatingRepository extends JpaRepository<LeadRating,Long> {
    LeadRating findLeadRatingByLeadRatingName(String leadRatingName);
}
