package org.epam.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TrainerDtoInput {

    @NonNull
    private Long trainingTypeId;

    @NonNull
    private Long userId;

    private List<Long> traineeIds;
}
