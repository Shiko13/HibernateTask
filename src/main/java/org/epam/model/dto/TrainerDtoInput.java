package org.epam.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TrainerDtoInput {

    @NonNull
    private Long trainingTypeId;

    @NonNull
    private Long userId;

    private List<Long> traineeIds;
}
