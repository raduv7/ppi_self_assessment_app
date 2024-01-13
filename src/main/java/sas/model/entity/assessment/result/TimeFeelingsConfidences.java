package sas.model.entity.assessment.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class TimeFeelingsConfidences {
    private Integer id;
    private Duration time;
    private final List<FeelingConfidence> feelingConfidenceList;

    public TimeFeelingsConfidences(Duration time) {
        this.time = time;
        this.feelingConfidenceList = new ArrayList<>();
    }

    public void addFeelingConfidence(FeelingConfidence feelingConfidence) {
        this.feelingConfidenceList.add(feelingConfidence);
    }
}
