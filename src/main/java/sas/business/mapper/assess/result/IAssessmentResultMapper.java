package sas.business.mapper.assess.result;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import sas.model.dto.assessment.AssessmentResultDto;
import sas.model.dto.assessment.AssessmentResultMetadataDto;
import sas.model.entity.assessment.result.AssessmentResult;
import sas.model.entity.assessment.result.AssessmentResultMetadata;

@Mapper
public interface IAssessmentResultMapper {
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "timeFeelingsConfidencesList", source = "timeFeelingsConfidencesList")
    })
    AssessmentResultDto toDto(AssessmentResult assessmentResult);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "hasAudio", source = "hasAudio"),
            @Mapping(target = "hasVideo", source = "hasVideo"),
            @Mapping(target = "hasWearableData", source = "hasWearableData")
    })
    AssessmentResultMetadataDto toDto(AssessmentResultMetadata assessmentResultMetadata);
}
