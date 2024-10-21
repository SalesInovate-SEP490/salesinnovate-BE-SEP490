package fpt.capstone.iAccount.repository;

import fpt.capstone.iAccount.model.AddressInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressInformationRepository extends JpaRepository<AddressInformation,Long> {
}
