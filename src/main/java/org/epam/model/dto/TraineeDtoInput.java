package org.epam.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TraineeDtoInput {

    private LocalDate dateOfBirth;

    private String address;

    private Long userId;

    private List<Long> trainerIds;
}
