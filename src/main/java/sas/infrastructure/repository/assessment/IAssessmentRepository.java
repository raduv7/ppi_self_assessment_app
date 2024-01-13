package sas.infrastructure.repository.assessment;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import sas.model.entity.assessment.result.AssessmentResult;
import sas.model.entity.assessment.result.AssessmentResultMetadata;
import sas.model.entity.auth.User;

import java.util.List;

@Repository
public interface IAssessmentRepository {
    String saveInputFile(User actor, Long id, MultipartFile file);
    String generateEmptyOutputFile(User actor, Long id);
    AssessmentResult getOneResult(User actor, Long id);
    Resource getOneInputVideo(User actor, Long id);
    List<AssessmentResultMetadata> getAllMetadata(User actor);
}
