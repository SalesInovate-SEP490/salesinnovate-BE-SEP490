package fpt.capstone.iAccount.repository;

import fpt.capstone.iAccount.model.Industry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndustryRepository extends JpaRepository<Industry,Long> {
    Industry findByIndustryStatusName(String name);
}
