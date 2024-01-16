package sas.model.dto.assessment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResultDto {
    private Timestamp id;
    private List<AssessmentResultFeelingDto> feelings = new ArrayList<>();

    public AssessmentResultDto(Timestamp id) {
        this.id = id;
    }

    public long getTimestamp() {
        return id.toInstant().getEpochSecond();
    }
}
