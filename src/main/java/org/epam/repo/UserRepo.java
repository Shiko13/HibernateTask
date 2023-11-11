package org.epam.repo;

import org.epam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    @Query("SELECT MAX(u.prefix) FROM User u WHERE u.userName = :userName")
    Integer findMaxPrefixByUsername(@Param("userName") String userName);

    boolean existsByUserName(String userName);

    Optional<User> findByUserNameAndPrefix(String userName, Integer prefix);
}
