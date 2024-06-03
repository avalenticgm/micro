package it.cgmconsulting.msauth.repository;

import it.cgmconsulting.msauth.entity.Role;
import it.cgmconsulting.msauth.entity.User;
import it.cgmconsulting.msauth.payload.response.SimpleUserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    boolean existsByUsernameOrEmail(String username, String email);

    @Query(value="SELECT u.username FROM User u WHERE u.id = :id")
    String getUsername(int id);

    @Query(value="SELECT new it.cgmconsulting.msauth.payload.response.SimpleUserResponse(" +
            "u.id, u.username " +
            ") FROM User u " +
            "WHERE u.id IN (:authorIds)")
    Set<SimpleUserResponse> getSimpleUsers(Set<Integer> authorIds);
}
