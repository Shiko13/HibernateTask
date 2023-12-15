package org.epam.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDtoInput {

    @NonNull
    private Long traineeId;

    @NonNull
    private Long trainerId;

    @NotBlank
    private String name;

    @NonNull
    private Long typeId;

    @NonNull
    private LocalDate date;

    @NonNull
    private Long duration;
}
