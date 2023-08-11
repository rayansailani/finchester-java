package kaif.finchesterjava.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kaif.finchesterjava.config.JwtService;
import kaif.finchesterjava.dto.UserDTO;
import kaif.finchesterjava.entities.Partner;
import kaif.finchesterjava.entities.User;
import kaif.finchesterjava.exceptions.ResourceNotFound;
import kaif.finchesterjava.service.PartnerService;
import kaif.finchesterjava.utilities.Utils;

@RestController
public class PartnerController {

    @Autowired
    private Utils utils;

    @Autowired
    private PartnerService partnerService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/partner/{partnerId}/createUser")
    public ResponseEntity<?> partnerSignup(@PathVariable("partnerId") Long partnerId, @RequestBody User user) {
        if (utils.isStringNullOrEmpty(user.getFullname()) || utils.isStringNullOrEmpty(user.getUsername()) ||
                utils.isStringNullOrEmpty(user.getEmail()) || utils.isLongNullOrEmpty(user.getMobile()) ||
                utils.isStringNullOrEmpty(user.getPassword()) || utils.isStringNullOrEmpty(user.getRole()) ||
                utils.isBoolNullOrEmpty(user.getIsAuthorized()) || utils.isStringNullOrEmpty(user.getGender())
                || utils.isLongNullOrEmpty(partnerId)) {
            throw new ResourceNotFound(
                    "Fullname, Username, Email, Mobile, Password, Role, isAuthorized, Gender and Partner id are mandatory fields");
        }

        utils.checkUserUniqueFields(user);
        if (user.getId() != null) {
            var res = partnerService.userSignup(partnerId, user);
            if (res == null) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User signup details not updated");
            }
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } else {
            var res = partnerService.userSignup(partnerId, user);
            if (res == null) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User not signed up");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }
    }


}
