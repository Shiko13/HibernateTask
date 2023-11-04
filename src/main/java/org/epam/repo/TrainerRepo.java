package org.epam.repo;

import org.epam.model.Trainer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TrainerRepo extends JpaRepository<Trainer, Long> {

    @EntityGraph(attributePaths = "trainees")
    Optional<Trainer> findByUserId(Long userId);

    @Query("SELECT t FROM Trainer t WHERE t.trainees IS EMPTY")
    List<Trainer> findTrainersWithEmptyTraineesList();
}
