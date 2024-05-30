package it.cgmconsulting.msauth.repository;

import it.cgmconsulting.msauth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    boolean existsByUsernameOrEmail(String username, String email);

    @Query(value="SELECT u.username FROM User u WHERE u.id = :id")
    String getUsername(int id);
}
