package org.epam.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDtoInput {

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private Boolean isActive;

}
