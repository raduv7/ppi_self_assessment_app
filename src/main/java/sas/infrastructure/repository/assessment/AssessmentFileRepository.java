package sas.infrastructure.repository.assessment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import sas.model.entity.assessment.result.AssessmentResult;
import sas.model.entity.assessment.result.AssessmentResultMetadata;
import sas.model.entity.auth.User;

import java.io.IOException;
import java.nio.file.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssessmentFileRepository implements IAssessmentRepository {
    @Value("${project_root.path}${user_data.path}")
    private String baseDirPath;

    private static final Logger log = LoggerFactory.getLogger(AssessmentFileRepository.class);

    @Override
    public AssessmentResult getAssessmentResult(User actor, Integer id) {
        return null;
    }

    @Override
    public List<AssessmentResultMetadata> getAssessmentResultMetadataList(User actor) {
        List<AssessmentResultMetadata> assessmentResultMetadataList = new ArrayList<>();
        String userDirPathStr = baseDirPath + actor.getId();
        Path userDirPath = Paths.get(userDirPathStr);

        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(userDirPath)) {
            dirStream.forEach(path -> {
                String fileName = path.getFileName().toString();
                String fileNameNoExtension = fileName.substring(0, fileName.lastIndexOf('.'));
                Timestamp timestamp = new Timestamp(Long.parseLong(fileNameNoExtension));

                assessmentResultMetadataList.add(new AssessmentResultMetadata(timestamp));
            });
        } catch (IOException | DirectoryIteratorException | NumberFormatException ex) {
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        return assessmentResultMetadataList;
    }
}
