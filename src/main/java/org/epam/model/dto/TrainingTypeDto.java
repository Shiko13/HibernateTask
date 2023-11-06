package org.epam.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class TrainingTypeDto {

    @NonNull
    private String name;

}
