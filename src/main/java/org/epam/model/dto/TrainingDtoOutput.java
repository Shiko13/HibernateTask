package org.epam.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDtoOutput {

    private Long id;

    private Long traineeId;

    private Long trainerId;

    private String name;

    private Long typeId;

    private LocalDate date;

    private Long duration;
}
