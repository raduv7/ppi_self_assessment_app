package sas.model.dto.assess;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sas.model.entity.assess.Assessment;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentBundleDto {
    private Assessment VideoAssessment;
    private Assessment AudioAssessment;
    private Assessment WearableAssessment;
}
