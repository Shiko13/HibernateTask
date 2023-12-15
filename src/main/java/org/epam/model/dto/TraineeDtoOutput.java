package org.epam.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeDtoOutput {

    private Long id;

    private LocalDate dateOfBirth;

    private String address;

    private UserDtoOutput user;

    private List<Long> trainerIds;
}
