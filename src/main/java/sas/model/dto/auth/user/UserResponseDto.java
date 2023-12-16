package sas.model.dto.auth.user;

import sas.model.entity.auth.Operation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private String username;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String profileName;
    private List<Operation> rightsOnEntity;
}
