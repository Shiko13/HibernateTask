package org.epam.repo;

import org.epam.model.Trainer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrainerRepo extends JpaRepository<Trainer, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "trainer-with-users-training-type-and-trainees-graph")
    Optional<Trainer> findByUserId(Long userId);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "trainer-with-users-training-type-and-trainees-graph")
    List<Trainer> findByTraineesIsEmpty();

}
