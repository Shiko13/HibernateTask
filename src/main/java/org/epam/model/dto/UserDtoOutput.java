package org.epam.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class UserDtoOutput {

    private Long id;

    private String firstName;

    private String lastName;

    private String userName;

    private Boolean isActive;

}
