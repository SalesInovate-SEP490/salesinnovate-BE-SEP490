package fpt.capstone.SalesInnovate.iLead.repository;

import fpt.capstone.SalesInnovate.iLead.model.AddressInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressInformationRepository extends JpaRepository<AddressInformation,Long> {
}
