package sas.model.entity.assessment.result;

import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class AssessmentResult {
    private Timestamp timestamp;
    private List<TimeFeelingsConfidences> timeFeelingsConfidencesList;

    public AssessmentResult() {
        this.timeFeelingsConfidencesList = new ArrayList<>();
    }

    public void addTimeFeelingConfidence(TimeFeelingsConfidences timeFeelingsConfidences) {
        this.timeFeelingsConfidencesList.add(timeFeelingsConfidences);
    }
}
