package sas.business.service.assessment;

import com.sun.jdi.InternalException;
import jakarta.annotation.PostConstruct;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sas.business._interface.service.IAssessmentService;
import sas.business.mapper.assess.result.IAssessmentResultMapper;
import sas.infrastructure.repository.assessment.IAssessmentRepository;
import sas.model.dto.assessment.AssessmentResultDto;
import sas.model.dto.assessment.AssessmentResultMetadataDto;
import sas.model.entity.assessment.result.AssessmentResult;
import sas.model.entity.assessment.result.AssessmentResultMetadata;
import sas.model.entity.auth.User;

import java.io.File;
import java.time.Instant;
import java.util.List;

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
    private IAssessmentRepository assessmentRepository;
    @Autowired
    private IAssessmentResultMapper assessmentResultMapper;

    @PostConstruct
    private void postConstruct() {
        System.out.println("Assessing absolute input folder path: " + inputDirPath);
        System.out.println("Assessing absolute output folder path: " + outputDirPath);
    }

    @Override
    public AssessmentResultDto create(User actor, MultipartFile file) {
        long assessmentId = Instant.now().getEpochSecond();
        String inputExtension = getFileExtension(file);
        String inputPath = generateInputPath(assessmentId, inputExtension);
        String outputPath = generateOutputPath(assessmentId);

        switch (inputExtension) {
            case "png":
                saveFile(file, inputPath);
                assessByRadu(inputPath, outputPath);
                break;
            case "mp3":
                saveFile(file, inputPath);
                assessByDiana(inputPath, outputPath);
                break;
            case "mp4":
                saveFile(file, inputPath);
                assessByRaul(inputPath, outputPath);
                break;
            case "waw":
                saveFile(file, inputPath);
                assessByLeo(inputPath, outputPath);
                break;
            default:
                throw new ServiceException("Sir, unsupported file extension.");
        }

        AssessmentResult result = assessmentRepository.getOneResult(actor, assessmentId);
        return assessmentResultMapper.toDto(result);
    }

    private String generateInputPath(long timestamp, String fileExtension) {
        File directory = new File(inputDirPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        return inputDirPath + timestamp + "." + fileExtension;
    }


    private String generateOutputPath(long timestamp) {
        File directory = new File(outputDirPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        return outputDirPath + timestamp + ".json";
    }

    private String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null && fileName.lastIndexOf(".") != -1) {
            return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        } else {
            return "";
        }
    }

    private void saveFile(MultipartFile file, String path) {
        try {
            file.transferTo(new File(path));
        }
        catch (Exception e) {
            throw new InternalException(e.getMessage());
        }
    }

    private void assessByRadu(String inputFilePath, String outputFilePath) {
        runPython("ai_model/model_radu/main.py", inputFilePath, outputFilePath);
    }

    private void assessByDiana(String inputFilePath, String outputFilePath) {
        runPython("ai_model/model_diana/main.py", inputFilePath, outputFilePath);
    }

    private void assessByRaul(String inputFilePath, String outputFilePath) {
        runPython("ai_model/model_raul/main.py", inputFilePath, outputFilePath);
    }

    private void assessByLeo(String inputFilePath, String outputFilePath) {
        runPython("ai_model/model_leo/main.py", inputFilePath, outputFilePath);
    }

    private void runPython(String pythonScriptPath, String inputFilePath, String outputFilePath) {
        try {
            ProcessBuilder pb = new ProcessBuilder("python", pythonScriptPath, inputFilePath, outputFilePath);
            Process p = pb.start();
            if(p.waitFor() != 0) {
                throw new Exception("");
            }
        }
        catch (Exception e) {
            throw new InternalException("Error while running AI model.");
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
