package sas.business.mapper.assess.result;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;
import sas.model.entity.assessment.result.AssessmentResult;
import sas.model.entity.assessment.result.TimeFeelingsConfidences;

import javax.management.InvalidAttributeValueException;
import java.io.*;
import java.time.Duration;
import java.util.List;

@Component
public class AssessmentResultMapper {
    private final ObjectMapper mapper;

    private void registerDurationDeserializer(ObjectMapper mapper) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Duration.class, new DurationDeserializer());
        mapper.registerModule(module);
    }

    public AssessmentResultMapper() {
        mapper = new ObjectMapper();
        registerDurationDeserializer(mapper);
    }

    public AssessmentResult parseAssessmentResultFromJsonFile(String filePath)
            throws InvalidAttributeValueException {
        try {
            List<TimeFeelingsConfidences> list = mapper.readValue(new File(filePath),
                    new TypeReference<List<TimeFeelingsConfidences>>() {});
            AssessmentResult result = new AssessmentResult();
            for (TimeFeelingsConfidences item : list) {
                result.addTimeFeelingConfidence(item);
            }
            return result;
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            throw new InvalidAttributeValueException("Sir, invalid file path or content.");
        }
    }
}
