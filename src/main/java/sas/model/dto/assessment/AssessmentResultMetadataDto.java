package sas.model.dto.assessment;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AssessmentResultMetadataDto {
    private Timestamp timestamp;
    private Boolean hasAudio;
    private Boolean hasVideo;
    private Boolean hasWearableData;
}
