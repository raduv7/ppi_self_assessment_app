package sas.model.dto.assessment;

import lombok.AllArgsConstructor;
import lombok.Data;
import sas.model.entity.assessment.result.TimeFeelingsConfidences;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
public class AssessmentResultDto {
    private Timestamp timestamp;
    private List<TimeFeelingsConfidences> timeFeelingsConfidencesList;
}
