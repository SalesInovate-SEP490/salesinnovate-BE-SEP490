package fpt.capstone.iContact.repository;

import fpt.capstone.iContact.model.CoOppRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CoOppRelationRepository extends JpaRepository<CoOppRelation,Long>{
    @Query(value = "select count (*) from CoOppRelation where opportunity_id = :opportunity_id and contact_id= :contact_id", nativeQuery = true)
    long countNumRelation (long opportunity_id,long contact_id);
}
