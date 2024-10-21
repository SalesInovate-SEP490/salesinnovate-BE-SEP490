package fpt.capstone.iUser.repository;

import fpt.capstone.iUser.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, String> {
}
