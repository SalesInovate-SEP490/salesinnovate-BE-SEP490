package fpt.capstone.iUser.repository;

import fpt.capstone.iUser.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,String> {
}
