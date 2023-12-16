package sas.api.controller;

import sas.model.dto.auth.user.UserChangePasswordDto;
import sas.model.dto.auth.user.UserCreateDto;
import sas.model.dto.auth.user.UserSignInDto;
import sas.model.exception_handling.FirstSignInException;
import sas.business._interface.service.IAuthenticationService;
import sas.model.entity.auth.User;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {
    @Autowired private IAuthenticationService authenticationService;

    @PostMapping("/sign_in")
    @SuppressWarnings("NONE")
    public @ResponseBody ResponseEntity<?> signIn(@RequestBody UserSignInDto userSignInDto) {
        try {
            String jwt = authenticationService.signIn(userSignInDto);
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }
        catch (FirstSignInException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.ACCEPTED);
        }
        catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/sign_up")
    @SuppressWarnings("NONE")
    public @ResponseBody ResponseEntity<?> signUp(@RequestBody UserCreateDto userCreateDto) {
        try {
            String jwt = authenticationService.signUp(userCreateDto);
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/sign_out")
    @SuppressWarnings("NONE")
    public @ResponseBody ResponseEntity<?> signOut(@RequestAttribute("actor") User actor) {
        try {
            authenticationService.signOut(actor);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{username}/password")
    @SuppressWarnings("NONE")
    public @ResponseBody ResponseEntity<?> changePassword(@RequestAttribute("actor") User actor, @RequestBody UserChangePasswordDto userChangePasswordDto) {
        try {
            return new ResponseEntity<>(authenticationService.changePassword(actor, userChangePasswordDto), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
