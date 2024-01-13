package sas.business.service.assessment;

import jakarta.annotation.PostConstruct;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sas.business._interface.service.IAssessmentService;
import sas.business.mapper.assess.result.IAssessmentResultMapper;
import sas.business.util.python_executor.PythonExecutorUtils;
import sas.infrastructure.repository.assessment.IAssessmentFileRepository;
import sas.model.dto.assessment.AssessmentResultDto;
import sas.model.dto.assessment.AssessmentResultMetadataDto;
import sas.model.entity.assessment.result.AssessmentResult;
import sas.model.entity.assessment.result.AssessmentResultMetadata;
import sas.model.entity.auth.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AssessmentService implements IAssessmentService {
    @Value("${ai_model.path}")
    public String modelDirPath;
    @Value("${project_root.path}${ai_input.path}")
    public String inputDirPath;
    @Value("${project_root.path}${ai_output.path}")
    public String outputDirPath;

    @Autowired
    @Qualifier("assessmentFileRepository")
    private IAssessmentFileRepository assessmentRepository;
    @Autowired
    private IAssessmentResultMapper assessmentResultMapper;

    @PostConstruct
    private void postConstruct() {
        System.out.println("Assessing absolute input folder path: " + inputDirPath);
        System.out.println("Assessing absolute output folder path: " + outputDirPath);
    }

    @Override
    public AssessmentResultDto create(User actor, MultipartFile audioFile,
                                      MultipartFile videoFile, MultipartFile wearableDataFile) {
        long newId = Instant.now().getEpochSecond();

        List<String> inputPaths = saveInputFiles(actor, newId, audioFile, videoFile, wearableDataFile);
        String outputPath = assessmentRepository.generateEmptyOutputFile(actor, newId);

        PythonExecutorUtils.runPython("ai_model/model_radu/main.py", inputPaths, outputPath);

        AssessmentResult result = assessmentRepository.getOneResult(actor, newId);
        return assessmentResultMapper.toDto(result);
    }

    private List<String> saveInputFiles(User actor, Long newId, MultipartFile audioFile,
                                        MultipartFile videoFile, MultipartFile wearableDataFile) {
        List<String> inputPaths = new ArrayList<>();

        saveInputPath(actor, newId, audioFile, inputPaths);
        saveInputPath(actor, newId, videoFile, inputPaths);
        saveInputPath(actor, newId, wearableDataFile, inputPaths);

        return inputPaths;
    }

    private void saveInputPath(User actor, Long newId, MultipartFile file, List<String> inputPaths) {
        if(file != null) {
            if(!Objects.equals(FilenameUtils.getExtension(file.getOriginalFilename()), "mp3")) {
                throw new InvalidFileNameException
                        (file.getOriginalFilename(), "Invalid file extension: not supported as audio file.");
            }

            String filePath = assessmentRepository.saveInputFile(actor, newId, file);
            inputPaths.add(filePath);
        }
    }

    @Override
    public AssessmentResultDto getOneResult(User actor, Long id) {
        AssessmentResult result = assessmentRepository.getOneResult(actor, id);
        return assessmentResultMapper.toDto(result);
    }

    @Override
    public Resource getOneInputVideo(User actor, Long id) {
        return assessmentRepository.getOneInputVideo(actor, id);
    }

    @Override
    public List<AssessmentResultMetadataDto> getAllMetadata(User actor) {
        List<AssessmentResultMetadata> assessmentResultMetadataList = assessmentRepository.getAllMetadata(actor);
        return assessmentResultMapper.toDtoList(assessmentResultMetadataList);
    }
}
