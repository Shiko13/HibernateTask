package org.epam.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoInput {

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private Boolean isActive;

}
