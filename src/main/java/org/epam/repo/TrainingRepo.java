package org.epam.repo;

import org.epam.model.Training;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TrainingRepo extends JpaRepository<Training, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "training-with-trainee-trainer-and-types-graph")
    List<Training> findByDateBetweenAndTraineeUserUserName(LocalDate startDate, LocalDate endDate, String traineeUsername);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "training-with-trainee-trainer-and-types-graph")
    List<Training> findByDateBetweenAndTrainerUserUserName(LocalDate startDate, LocalDate endDate, String trainerUsername);
}