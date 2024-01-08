package sas.model.entity.assess.result;

import lombok.Data;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Data
public class TimeFeelingConfidence {
    private Duration time;
    private final List<FeelingConfidence> feelingConfidenceList;

    public TimeFeelingConfidence(Duration time) {
        this.time = time;
        this.feelingConfidenceList = new ArrayList<>();
    }

    public void addFeelingConfidence(FeelingConfidence feelingConfidence) {
        this.feelingConfidenceList.add(feelingConfidence);
    }
}
