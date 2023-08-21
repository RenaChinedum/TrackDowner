package com.ntechinedum.TrackDowner.dto;

import jakarta.persistence.Column;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String  firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;

}
