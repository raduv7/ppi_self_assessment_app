package sas.business.service.assessment;

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
import sas.business.util.pythonExecutor.PythonExecutorUtils;
import sas.infrastructure.repository.assessment.IAssessmentFileRepository;
import sas.model.dto.assessment.AssessmentResultDto;
import sas.model.dto.assessment.AssessmentResultMetadataDto;
import sas.model.entity.assessment.result.AssessmentResult;
import sas.model.entity.assessment.result.AssessmentResultMetadata;
import sas.model.entity.auth.User;

import java.security.InvalidParameterException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class AssessmentService implements IAssessmentService {
    @Value("${ai_model.path}")
    public String modelDirPath;

    @Autowired
    @Qualifier("assessmentFileRepository")
    private IAssessmentFileRepository assessmentRepository;
    @Autowired
    @Qualifier("assessmentResultMapperExtendedImpl")
    private IAssessmentResultMapper assessmentResultMapper;


    @Override
    public AssessmentResultDto create(User actor, MultipartFile audioFile,
                                      MultipartFile videoFile, MultipartFile wearableDataFile) {
        long newId = Instant.now().getEpochSecond();

        List<String> inputPaths = saveInputFiles(actor, newId, audioFile, videoFile, wearableDataFile);
        String outputPath = assessmentRepository.generateEmptyOutputFile(actor, newId);

        PythonExecutorUtils.runPython(modelDirPath, inputPaths, outputPath);

        AssessmentResult result = assessmentRepository.getOneResult(actor, newId);
        return assessmentResultMapper.toDto(result);
    }

    private List<String> saveInputFiles(User actor, Long newId, MultipartFile audioFile,
                                        MultipartFile videoFile, MultipartFile wearableDataFile) {
        List<String> inputPaths = new ArrayList<>();

        saveInputPath(actor, newId, audioFile, inputPaths, List.of("mp4"));
        saveInputPath(actor, newId, videoFile, inputPaths, List.of("mp3"));
        saveInputPath(actor, newId, wearableDataFile, inputPaths, List.of("csv"));

        if (inputPaths.isEmpty())
            throw new InvalidParameterException("No input files provided.");

        return inputPaths;
    }

    private void saveInputPath(User actor, Long newId, MultipartFile file, List<String> inputPaths, List<String> allowedExtensions) {
        if(file != null) {
            if(!allowedExtensions.contains(FilenameUtils.getExtension(file.getOriginalFilename()))) {
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
        assessmentResultMetadataList.sort(Comparator.comparing(AssessmentResultMetadata::getId));
        return assessmentResultMapper.toDtoList(assessmentResultMetadataList);
    }
}
