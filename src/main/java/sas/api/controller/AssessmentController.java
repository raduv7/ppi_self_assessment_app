package sas.api.controller;

import org.springframework.web.multipart.MultipartFile;
import sas.business._interface.service.IAssessmentService;
import sas.model.entity.assessment.result.AssessmentResult;
import sas.model.entity.auth.User;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assessment")
@CrossOrigin(origins = "*")
public class AssessmentController {
    @Autowired private IAssessmentService assessmentService;

    @PostMapping("")
    @SuppressWarnings({"NONE", "CallToPrintStackTrace"})
    public @ResponseBody ResponseEntity<?> create(@RequestAttribute("actor") User actor,
                                                  @RequestParam("file") MultipartFile file) {
        try {
            AssessmentResult assessmentResult = assessmentService.assess(file);
            return new ResponseEntity<>(assessmentResult, HttpStatus.OK);
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

    @GetMapping("/{timestamp}")
    public @ResponseBody ResponseEntity<?> get(@RequestAttribute("actor") User actor,
                                                @PathVariable("timestamp") Long timestamp) {
        try {
            AssessmentResult assessmentResult = assessmentService.getOne(actor, timestamp);
            return new ResponseEntity<>(assessmentResult, HttpStatus.OK);
        }
        catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
