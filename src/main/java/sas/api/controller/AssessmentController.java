package sas.api.controller;

import org.springframework.web.multipart.MultipartFile;
import sas.business._interface.service.IAssessmentService;
import sas.model.dto.assessment.AssessmentResultDto;
import sas.model.entity.auth.User;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assessments")
@CrossOrigin(origins = "*")
public class AssessmentController {
    @Autowired private IAssessmentService assessmentService;

    @PostMapping("")
    @SuppressWarnings({"NONE", "CallToPrintStackTrace"})
    public @ResponseBody ResponseEntity<?> create(@RequestAttribute("actor") User actor,
                                                  @RequestParam("file") MultipartFile file) {
        try {
            AssessmentResultDto assessmentResultDto = assessmentService.create(actor, file);
            return new ResponseEntity<>(assessmentResultDto, HttpStatus.OK);
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

    @GetMapping("/{id}/inputs/video")
    public @ResponseBody ResponseEntity<?> getOneInputVideo(@RequestAttribute("actor") User actor,
                                                             @PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(assessmentService.getOneInputVideo(actor, id), HttpStatus.OK);
        }
        catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/result")
    public @ResponseBody ResponseEntity<?> getOneResult(@RequestAttribute("actor") User actor,
                                                @PathVariable("id") Long id) {
        try {
            AssessmentResultDto assessmentResultDto = assessmentService.getOneResult(actor, id);
            return new ResponseEntity<>(assessmentResultDto, HttpStatus.OK);
        }
        catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping
    public @ResponseBody ResponseEntity<?> getAll(@RequestAttribute("actor") User actor) {
        try {
            return new ResponseEntity<>(assessmentService.getAllMetadata(actor), HttpStatus.OK);
        }
        catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}