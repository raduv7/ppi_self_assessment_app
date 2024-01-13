package sas.infrastructure.repository.assessment;

import org.springframework.stereotype.Repository;
import sas.model.entity.assessment.result.AssessmentResult;
import sas.model.entity.assessment.result.AssessmentResultMetadata;
import sas.model.entity.auth.User;

import java.util.List;

@Repository
public interface IAssessmentRepository {
    AssessmentResult getAssessmentResult(User actor, Integer id);
    List<AssessmentResultMetadata> getAssessmentResultMetadataList(User actor);
}
