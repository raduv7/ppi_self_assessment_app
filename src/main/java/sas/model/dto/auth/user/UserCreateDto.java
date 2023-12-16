package sas.model.dto.auth.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String profileName;
}
