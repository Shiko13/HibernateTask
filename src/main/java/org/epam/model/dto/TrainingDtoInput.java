package org.epam.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDtoInput {

    @NonNull
    private Long traineeId;

    @NonNull
    private Long trainerId;

    @NonNull
    private String name;

    @NonNull
    private Long typeId;

    @NonNull
    private LocalDate date;

    @NonNull
    private Long duration;
}
