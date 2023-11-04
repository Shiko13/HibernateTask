package org.epam.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
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
