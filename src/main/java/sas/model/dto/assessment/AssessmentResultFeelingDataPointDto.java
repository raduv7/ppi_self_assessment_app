package sas.model.dto.assessment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResultFeelingDataPointDto {
    private long name;          // time
    private int value;          // confidence
}
