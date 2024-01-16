package sas.business.mapper.assess.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;
import sas.model.dto.assessment.AssessmentResultDto;
import sas.model.dto.assessment.AssessmentResultFeelingDataPointDto;
import sas.model.dto.assessment.AssessmentResultFeelingDto;
import sas.model.entity.assessment.result.AssessmentResult;
import sas.model.entity.assessment.result.EFeeling;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class AssessmentResultMapperExtendedImpl extends IAssessmentResultMapperImpl {
    @Override
    public AssessmentResultDto toDto(AssessmentResult entity) {
        Map<EFeeling, List<AssessmentResultFeelingConfidenceTimestamp>> map =
                entity.getTimeFeelingsConfidencesList()
                        .stream()
                        .flatMap(timeFeelingsConfidences ->
                                timeFeelingsConfidences.getFeelingConfidenceList()
                                        .stream()
                                        .map(feelingConfidence ->
                                                new AssessmentResultFeelingConfidenceTimestamp(
                                                        feelingConfidence.getFeeling(),
                                                        feelingConfidence.getConfidence(),
                                                        timeFeelingsConfidences.getTime()
                                                )
                                        )
                        )
                        .collect(Collectors.groupingBy(AssessmentResultFeelingConfidenceTimestamp::getFeeling));

        AssessmentResultDto dto = map.entrySet()
                .stream()
                .map((entry) ->
                        new AssessmentResultFeelingDto(
                                this.toCamelCaseString(entry.getKey().toString()),
                                entry.getValue()
                                        .stream()
                                        .map(this::toAssessmentResultFeelingDataPointDto)
                                        .collect(Collectors.toList())
                        )

                )
                .reduce(
                        new AssessmentResultDto(),
                        (assessmentResultDto, assessmentResultFeelingDto) -> {
                            assessmentResultDto.getFeelings().add(assessmentResultFeelingDto);
                            return assessmentResultDto;
                        },
                        (assessmentResultDto1, assessmentResultDto2) -> {
                            assessmentResultDto1.getFeelings().addAll(assessmentResultDto2.getFeelings());
                            return assessmentResultDto1;
                        }
                );

        dto.setId(entity.getId());

        return dto;
    }

    private AssessmentResultFeelingDataPointDto toAssessmentResultFeelingDataPointDto
            (AssessmentResultFeelingConfidenceTimestamp assessmentResultFeelingConfidenceTimestamp) {
        return new AssessmentResultFeelingDataPointDto(
                assessmentResultFeelingConfidenceTimestamp.getTimestamp().toMillis(),
                assessmentResultFeelingConfidenceTimestamp.getConfidence()
        );
    }

    private String toCamelCaseString(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    @Data
    @AllArgsConstructor
    private static class AssessmentResultFeelingConfidenceTimestamp {
        private EFeeling feeling;
        private Integer confidence;
        private Duration timestamp;
    }
}
