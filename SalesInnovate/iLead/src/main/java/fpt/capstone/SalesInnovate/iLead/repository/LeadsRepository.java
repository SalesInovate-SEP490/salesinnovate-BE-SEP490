package fpt.capstone.SalesInnovate.iLead.repository;

import fpt.capstone.SalesInnovate.iLead.model.Leads;
import fpt.capstone.SalesInnovate.iLead.repository.specification.SpecSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadsRepository extends JpaRepository<Leads,Long>, JpaSpecificationExecutor<Leads> {

    @Query(value = "SELECT * FROM leads WHERE status_id = ?1", nativeQuery = true)
    List<Leads> findByStatus(long statusId);
}
