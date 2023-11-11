package org.epam.model.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TraineeDtoInput {

    private Long id;

    private LocalDate dateOfBirth;

    @Size(max = 255)
    private String address;

    @NonNull
    private Long userId;

    private List<Long> trainerIds;
}
