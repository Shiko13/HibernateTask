package org.epam.repo;

import org.epam.model.Trainee;
import org.epam.model.Trainer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainerRepo extends JpaRepository<Trainer, Long> {

    @EntityGraph(attributePaths = "trainees")
    Optional<Trainer> findByUserId(Long userId);
}
