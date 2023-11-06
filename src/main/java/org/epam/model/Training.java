package org.epam.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "trainings")
public class Training {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trainee_id")
    private Long traineeId;

    @Column(name = "trainer_id")
    private Long trainerId;

    @Column(name = "name")
    private String name;

    @Column(name = "type_id")
    private Long typeId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "duration")
    private Long duration;
}
