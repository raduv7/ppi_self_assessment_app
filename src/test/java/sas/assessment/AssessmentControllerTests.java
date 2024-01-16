package sas.assessment;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import sas.api.controller.AssessmentController;
import sas.infrastructure.repository.auth.IUserRepository;
import sas.model.entity.auth.User;

@SpringBootTest
public class AssessmentControllerTests {
    @Autowired
    private AssessmentController assessmentController;
    @Autowired
    private IUserRepository userRepository;
    private User actor;

    @BeforeEach
    public void setUp() {
        actor = userRepository.findByUsername("r1").orElseThrow();
    }

    @Test
    public void testGetAll() {
         Assertions.assertEquals(assessmentController.getAll(actor).getStatusCode(), HttpStatus.OK);
    }
}
