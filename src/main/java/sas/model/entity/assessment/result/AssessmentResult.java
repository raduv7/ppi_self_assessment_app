package sas.model.entity.assessment.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class AssessmentResult {
    private Timestamp id;
    private List<TimeFeelingsConfidences> timeFeelingsConfidencesList;

    public AssessmentResult() {
        this.timeFeelingsConfidencesList = new ArrayList<>();
    }

    public void addTimeFeelingConfidence(TimeFeelingsConfidences timeFeelingsConfidences) {
        this.timeFeelingsConfidencesList.add(timeFeelingsConfidences);
    }
}
