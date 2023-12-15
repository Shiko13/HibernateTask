package org.epam.repo;

import org.epam.model.Trainee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TraineeRepo extends JpaRepository<Trainee, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "trainee-with-users-and-trainers-graph")
    Optional<Trainee> findByUserId(Long userId);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "trainee-with-trainers-graph")
    List<Trainee> findAllByIdIn(List<Long> ids);
}
