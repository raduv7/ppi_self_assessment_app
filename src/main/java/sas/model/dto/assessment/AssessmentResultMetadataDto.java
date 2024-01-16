package sas.model.dto.assessment;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AssessmentResultMetadataDto {
    private Timestamp id;
    private Boolean hasAudio;
    private Boolean hasVideo;
    private Boolean hasWearableData;

    @SuppressWarnings("unused")
    public long getTimestamp() {
        return id.toInstant().getEpochSecond();
    }
}
