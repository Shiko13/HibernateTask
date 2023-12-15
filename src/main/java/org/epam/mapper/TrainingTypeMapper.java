package org.epam.mapper;

import org.epam.model.TrainingType;
import org.epam.model.dto.TrainingTypeOutputDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainingTypeMapper {

    TrainingTypeOutputDto toDto(TrainingType trainingType);
}
