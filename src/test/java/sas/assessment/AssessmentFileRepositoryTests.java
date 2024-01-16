package sas.assessment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import sas.infrastructure.repository.assessment.IAssessmentFileRepository;
import sas.infrastructure.repository.auth.IUserRepository;
import sas.model.entity.assessment.result.AssessmentResult;
import sas.model.entity.auth.User;

@SpringBootTest
public class AssessmentFileRepositoryTests {
    @Autowired
    @Qualifier("assessmentFileRepository")
    private IAssessmentFileRepository assessmentFileRepository;
    @Autowired
    private IUserRepository userRepository;

    @Test
    public void testParseResultFromJson() {
        User actor = userRepository.findByUsername("r1").orElseThrow();
        AssessmentResult assessmentResult = assessmentFileRepository.getOneResult(actor, 1705405292L);
        Assertions.assertNotNull(assessmentResult);
    }
}
