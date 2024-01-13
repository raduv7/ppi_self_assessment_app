package sas.business.mapper.assess.result;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import sas.model.dto.assessment.AssessmentResultDto;
import sas.model.dto.assessment.AssessmentResultMetadataDto;
import sas.model.entity.assessment.result.AssessmentResult;
import sas.model.entity.assessment.result.AssessmentResultMetadata;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IAssessmentResultMapper {
    AssessmentResultDto toDto(AssessmentResult assessmentResult);

    AssessmentResultMetadataDto toDto(AssessmentResultMetadata assessmentResultMetadata);

    List<AssessmentResultMetadataDto> toDtoList(List<AssessmentResultMetadata> assessmentResultMetadataList);
}
