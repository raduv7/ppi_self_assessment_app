package sas.model.dto.assessment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResultFeelingDto {
    private String name;
    private List<AssessmentResultFeelingDataPointDto> series;
}
