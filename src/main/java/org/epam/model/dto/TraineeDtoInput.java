package org.epam.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TraineeDtoInput {

    private LocalDate dateOfBirth;

    private String address;

    private Long userId;

    private List<Long> trainerIds;
}
