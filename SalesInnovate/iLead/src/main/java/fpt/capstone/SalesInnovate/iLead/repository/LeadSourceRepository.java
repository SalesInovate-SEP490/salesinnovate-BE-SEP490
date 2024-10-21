package fpt.capstone.SalesInnovate.iLead.repository;

import fpt.capstone.SalesInnovate.iLead.model.LeadSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadSourceRepository extends JpaRepository<LeadSource,Long> {
    LeadSource findLeadSourceByLeadSourceName(String leadSourceName);
}
