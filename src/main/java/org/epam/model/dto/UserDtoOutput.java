package org.epam.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class UserDtoOutput {

    private Long id;

    private String firstName;

    private String lastName;

    private String userName;

    private Boolean isActive;

}
