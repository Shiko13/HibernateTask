package org.epam.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.epam.model.User;

import java.time.LocalDate;
import java.util.List;

@Getter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TraineeDtoOutput {

    private Long id;

    private LocalDate dateOfBirth;

    private String address;

    private UserDtoOutput user;

    private List<Long> trainerIds;
}
