package sas.model.entity.assessment.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeFeelingsConfidences {
    private Integer id;
    private Duration time;
    private final List<FeelingConfidence> feelingConfidenceList = new ArrayList<>();

    public TimeFeelingsConfidences(Duration time) {
        this.time = time;
    }

    public void addFeelingConfidence(FeelingConfidence feelingConfidence) {
        this.feelingConfidenceList.add(feelingConfidence);
    }
}
