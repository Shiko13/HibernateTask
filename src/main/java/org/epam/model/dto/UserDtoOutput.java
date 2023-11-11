package org.epam.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoOutput {

    private Long id;

    private String firstName;

    private String lastName;

    private String userName;

    private Boolean isActive;

}
