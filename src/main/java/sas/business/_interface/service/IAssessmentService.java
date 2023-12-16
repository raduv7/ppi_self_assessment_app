package sas.business._interface.service;

import org.springframework.web.multipart.MultipartFile;

public interface IAssessmentService {
    String assess(MultipartFile file);
}
