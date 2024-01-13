package sas.model.entity.assessment.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResultMetadata {
    private Timestamp timestamp;
}
