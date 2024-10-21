package fpt.capstone.iAccount.repository;

import fpt.capstone.iAccount.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {
    AccountType findByAccountTypeName(String name);
}
