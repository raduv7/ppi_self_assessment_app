package sas.model.dto.auth.profile;

import sas.model.entity.auth.Operation;
import lombok.Data;

import java.util.List;

@Data
public class ProfileRequestDto {
    private String name;
    private List<Operation> rights;
}
