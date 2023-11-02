package org.epam.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class TrainingDto {

    @NonNull
    private Long traineeId;

    @NonNull
    private Long trainerId;

    @NonNull
    private String name;

    @NonNull
    private Long trainingTypeId;

    @NonNull
    private LocalDate date;

    @NonNull
    private Long duration;
}
