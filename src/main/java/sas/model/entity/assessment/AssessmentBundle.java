package sas.model.entity.assessment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentBundle {
    private Assessment VideoAssessment;
    private Assessment AudioAssessment;
    private Assessment WearableAssessment;
}
