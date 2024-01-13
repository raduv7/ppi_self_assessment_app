package sas.business._interface.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import sas.model.dto.assessment.AssessmentResultDto;
import sas.model.dto.assessment.AssessmentResultMetadataDto;
import sas.model.entity.auth.User;

import java.util.List;

public interface IAssessmentService {
    AssessmentResultDto create(User actor, MultipartFile file);
    AssessmentResultDto getOneResult(User actor, Long id);
    Resource getOneInputVideo(User actor, Long id);
    List<AssessmentResultMetadataDto> getAllMetadata(User actor);
}
