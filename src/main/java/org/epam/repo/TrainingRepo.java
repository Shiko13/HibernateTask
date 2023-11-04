package org.epam.repo;

import org.epam.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TrainingRepo extends JpaRepository<Training, Long> {

    @Query(value = "SELECT t.id, t.name, tr.id AS trainee_id, t.trainer_id, t.type_id, t.date, t.duration\n" +
            "FROM trainings AS t\n" +
            "INNER JOIN trainees AS tr ON t.trainee_id = tr.id\n" + "WHERE t.date BETWEEN :startDate AND :endDate\n" +
            "AND tr.user_id = (SELECT u.id FROM users u WHERE u.user_name = :traineeUsername)",
           nativeQuery = true)
    List<Training> findByDateRangeAndTraineeUsername(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("traineeUsername") String traineeUsername);

    @Query(value = "SELECT t.id, t.name, tr.id AS trainer_id, t.trainee_id, t.type_id, t.date, t.duration\n" +
            "FROM trainings AS t\n" +
            "INNER JOIN trainers AS tr ON t.trainer_id = tr.id\n" + "WHERE t.date BETWEEN :startDate AND :endDate\n" +
            "AND tr.user_id = (SELECT u.id FROM users u WHERE u.user_name = :trainerUsername)",
           nativeQuery = true)
    List<Training> findByDateRangeAndTrainerUsername(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("trainerUsername") String trainerUsername);
}
