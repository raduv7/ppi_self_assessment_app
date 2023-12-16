package sas.api.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import sas.business._interface.service.IAssessmentService;
import sas.model.entity.auth.User;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;

@RestController
@RequestMapping("/assessment")
@CrossOrigin(origins = "*")
public class AssessmentController {
    @Autowired private IAssessmentService assessmentService;

    @PostMapping("")
    @SuppressWarnings("NONE")
    public @ResponseBody ResponseEntity<?> getOps(@RequestAttribute("actor") User actor,
                                                  @RequestParam("file") MultipartFile file) {
        try {
            String filePath = assessmentService.assess(file);

            File fileToReturn = new File(filePath);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(fileToReturn));

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .header("Content-Disposition", "attachment; filename=\"" + fileToReturn.getName() + "\"")
                    .body(resource);
        }
        catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
