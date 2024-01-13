package sas.model.dto.assessment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sas.model.entity.assessment.Assessment;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentBundleDto {
    private Assessment VideoAssessment;
    private Assessment AudioAssessment;
    private Assessment WearableAssessment;
}
