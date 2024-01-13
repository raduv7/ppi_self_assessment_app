package sas.infrastructure.repository.assessment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sun.jdi.InternalException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import sas.business.mapper.assess.result.DurationDeserializer;
import sas.model.entity.assessment.result.AssessmentResult;
import sas.model.entity.assessment.result.AssessmentResultMetadata;
import sas.model.entity.assessment.result.TimeFeelingsConfidences;
import sas.model.entity.auth.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class AssessmentFileRepository implements IAssessmentFileRepository {
    @Value("${project_root.path}${user_data.path}")
    private String baseDirPath;

    private final ObjectMapper mapper = new ObjectMapper();

    private static final Logger log = LoggerFactory.getLogger(AssessmentFileRepository.class);


    @PostConstruct
    public void postConstruct() {
        registerDurationDeserializer(mapper);
        ensureDirExists(baseDirPath);
        log.info("Assessment file repository initialized in ." + baseDirPath);
    }

    private void registerDurationDeserializer(ObjectMapper mapper) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Duration.class, new DurationDeserializer());
        mapper.registerModule(module);
    }

    @Override
    public String saveInputFile(User actor, Long id, MultipartFile file) {
        try {
            String filePathStr = generateInputFilePath(actor, id, file);
            Path filePath = Paths.get(filePathStr);
            ensureDirExists(filePath.getParent());
            file.transferTo(filePath);

            return filePathStr;
        } catch (IOException e) {
            log.error(e.toString());
            throw new InternalException(e.getMessage());
        }
    }

    private String generateInputFilePath(User actor, Long id, MultipartFile file) {
        return baseDirPath + actor.getId() + '/' + id + '/' +
                "input" + '.' + FilenameUtils.getExtension(file.getOriginalFilename());
    }

    @Override
    public String generateEmptyOutputFile(User actor, Long id) {
        String filePathStr = generateOutputFilePath(actor, id);
        Path filePath = Paths.get(filePathStr);

        try {
            ensureDirExists(filePath.getParent());
            Files.createFile(filePath);
        } catch (IOException e) {
            log.error(e.toString());
            throw new InternalException(e.getMessage());
        }

        return filePathStr;
    }

    private String generateOutputFilePath(User actor, Long id) {
        return baseDirPath + actor.getId() + '/' + id + '/' + "output.json";
    }

    @Override
    public AssessmentResult getOneResult(User actor, Long id) {
        String resultPathStr = baseDirPath + actor.getId() + '/' + id + '/' + "output.json";
        Path resultPath = Paths.get(resultPathStr);

        try {
            if (!Files.exists(resultPath) || !Files.isRegularFile(resultPath)) {
                throw new NoSuchFileException(resultPath.toString());
            }
            return parseAssessmentResult(
                    Files.newBufferedReader(resultPath));
        } catch (IOException e) {
            log.error(e.toString());
        }
        return null;
    }

    private AssessmentResult parseAssessmentResult(BufferedReader reader)
            throws IOException {
        List<TimeFeelingsConfidences> list = mapper.readValue(reader,
                new TypeReference<>() {
                });
        AssessmentResult result = new AssessmentResult();
        for (TimeFeelingsConfidences item : list) {
            result.addTimeFeelingConfidence(item);
        }
        return result;
    }

    @Override
    public Resource getOneInputVideo(User actor, Long id) {
        String videoPathStr = baseDirPath + actor.getId() + '/' + id + '/' + "input.mp4";
        Path videoPath = Paths.get(videoPathStr);

        try {
            if (!Files.exists(videoPath) || !Files.isRegularFile(videoPath)) {
                throw new NoSuchFileException(videoPath.toString());
            }

            return new UrlResource(videoPath.toUri());
        } catch (MalformedURLException | NoSuchFileException e) {
            log.error(e.toString());
        }
        return null;
    }

    @Override
    public List<AssessmentResultMetadata> getAllMetadata(User actor) {
        String userDirPathStr = baseDirPath + actor.getId();
        Path userDirPath = Paths.get(userDirPathStr);

        try (Stream<Path> dirStream = Files.list(userDirPath)) {
            return dirStream.reduce(new ArrayList<>(),
                    (list, path) -> {
                        try {
                            list.add(getAssessmentResultMetadata(path));
                        } catch (IOException e) {
                            log.error(Arrays.toString(e.getStackTrace()));
                        }
                        return list;
                    },
                    (list1, list2) -> {
                        list1.addAll(list2);
                        return list1;
                    });
        } catch (IOException | DirectoryIteratorException | NumberFormatException e) {
            log.error(e.toString());
        }

        return new ArrayList<>();
    }

    private AssessmentResultMetadata getAssessmentResultMetadata(Path assessmentDirPath)
            throws IOException {
        if (Files.isRegularFile(assessmentDirPath)) {
            throw new NotDirectoryException(assessmentDirPath.toString());
        }

        try (Stream<Path> files = Files.list(assessmentDirPath)) {
            return getAssessmentResultMetadataFromFiles(files, assessmentDirPath);
        }
    }

    private AssessmentResultMetadata getAssessmentResultMetadataFromFiles
            (Stream<Path> files, Path assessmentDirPath)
            throws IOException {
        boolean hasJson = false;
        boolean hasAudio = false;
        boolean hasVideo = false;
        boolean hasWearableData = false;

        for (Path file : files.toArray(Path[]::new)) {
            String extension = FilenameUtils.getExtension(file.toString());
            switch (extension) {
                case "mp4" -> hasVideo = true;
                case "wav" -> hasAudio = true;
                case "csv" -> hasWearableData = true;
                case "json" -> hasJson = true;
            }
        }
        if (!hasJson) {
            throw new NoSuchFileException("No result file found in " + assessmentDirPath.toString());
        }
        Timestamp id = Timestamp.valueOf(assessmentDirPath.getFileName().toString());

        return new AssessmentResultMetadata(id, hasAudio, hasVideo, hasWearableData);
    }

    private static void ensureDirExists(String dirPathStr) {
        Path dirPath = Paths.get(dirPathStr);
        ensureDirExists(dirPath);
    }

    private static void ensureDirExists(Path dirPath) {
        if (!Files.exists(dirPath)) {
            try {
                Files.createDirectory(dirPath);
            } catch (IOException e) {
                log.error(e.toString());
                throw new InternalException(e.getMessage());
            }
        }
    }
}
