package sas.model.entity.assess.result;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AssessmentResult {
    private List<TimeFeelingConfidence> timeFeelingConfidenceList;

    public AssessmentResult() {
        this.timeFeelingConfidenceList = new ArrayList<>();
    }

    public void addTimeFeelingConfidence(TimeFeelingConfidence timeFeelingConfidence) {
        this.timeFeelingConfidenceList.add(timeFeelingConfidence);
    }
}
