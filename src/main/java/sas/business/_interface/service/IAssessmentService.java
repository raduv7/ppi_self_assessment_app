package sas.business._interface.service;

import org.springframework.web.multipart.MultipartFile;

public interface IAssessmentService {
    AssessmentResultDto create(MultipartFile file);
    AssessmentResultDto getOne(Long timestamp);
    List<AssessmentResultDto> getAll();

}
