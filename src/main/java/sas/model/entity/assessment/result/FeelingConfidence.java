package sas.model.entity.assessment.result;

import lombok.*;

import java.security.InvalidParameterException;

@Data
public class FeelingConfidence {
    private EFeeling feeling;
    private Integer confidence;

    public FeelingConfidence(EFeeling feeling, Integer confidence) {
        this.feeling = feeling;
        this.setConfidence(confidence);
    }

    public void setConfidence(Integer confidence) {
        if (confidence < 0) {
            throw new InvalidParameterException("Sir, confidence must not be below 0!");
        } else if (confidence > 100) {
            throw new InvalidParameterException("Sir, confidence must not be above 100!");
        }
        this.confidence = confidence;
    }
}
