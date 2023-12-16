package sas.model.dto.auth.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private String username;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String profileName;
}
