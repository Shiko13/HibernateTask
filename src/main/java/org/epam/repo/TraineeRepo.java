package org.epam.repo;

import org.epam.model.Trainee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TraineeRepo extends JpaRepository<Trainee, Long> {

    @EntityGraph(attributePaths = "trainers")
    Optional<Trainee> findByUserId(Long userId);
}
