package sas.model.dto.auth.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordDto {
    private String oldPassword;
    private String newPassword;
}
