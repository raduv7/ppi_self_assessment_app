package sas.model.dto.auth.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import sas.model.entity.auth.Operation;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileResponseDto {
    private String name;
    private List<Operation> rights;
    private List<Operation> rightsOnEntity;
}
