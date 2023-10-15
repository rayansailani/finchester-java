package kaif.finchesterjava.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.validation.Valid;

import kaif.finchesterjava.config.JwtService;
import kaif.finchesterjava.dto.UserDTO;
import kaif.finchesterjava.entities.Partner;
import kaif.finchesterjava.entities.User;
import kaif.finchesterjava.exceptions.ResourceNotFound;
import kaif.finchesterjava.repositories.PartnerRepo;
import kaif.finchesterjava.repositories.UserRepo;
import kaif.finchesterjava.utilities.Utils;

@RestController
public class UserController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Utils utils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PartnerRepo partnerRepo;

    // tempapory signup for admin
    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        if (utils.isStringNullOrEmpty(user.getFullname()) || utils.isStringNullOrEmpty(user.getEmail()) ||
                utils.isLongNullOrEmpty(user.getMobile()) || utils.isStringNullOrEmpty(user.getPassword()) ||
                utils.isStringNullOrEmpty(user.getRole()) || utils.isBoolNullOrEmpty(user.getIsAuthorized())) {
            throw new ResourceNotFound(
                    "Fullname, Email, Mobile, Password, Role and isAuthorized are mandatory required fields");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User res = userRepo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);

    }

    // @PostMapping("/signin")
    // public ResponseEntity<?> authenticateAndGetToken(@RequestBody User user) {
    //     Map<String, Object> map = new HashMap<>();
    //     try {

    //         User userData = utils.fetchUserByMobileOrEmail(user.getUsername());

    //         Authentication authentication = authenticationManager
    //                 .authenticate(new UsernamePasswordAuthenticationToken(userData.getUsername(),
    //                         user.getPassword()));

    //         if (authentication.isAuthenticated()) {
    //             if (userData.getIsAuthorized().equals(false)) {
    //                 map.put("message", "User not authorized");
    //                 return new ResponseEntity<>(map, HttpStatus.NOT_ACCEPTABLE);
    //             }
    //         }

    //         String token = jwtService.generateToken(userData.getUsername());
    //         // System.out
    //         //         .println("***************************************************************************************");
    //         // System.out.println(userData);
    //         // System.out
    //         //         .println("***************************************************************************************");
    //         var userDto = new UserDTO(userData.getId(), userData.getFullname(),
    //                 userData.getFullname(), userData.getEmail(), userData.getMobile());
    //         map.put("role", userData.getRole());
    //         map.put("user", userDto);
    //         map.put("token", token);
    //         return new ResponseEntity<>(map, HttpStatus.OK);
    //     } catch (Exception e) {
    //         map.put("message", "Invalid credentials");
    //         return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    //     }
    // }
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody User user) {
    Map<String, Object> map = new HashMap<>();
    try {

    User userData = utils.fetchUserByMobileOrEmail(user.getUsername());

    Authentication authentication = authenticationManager
    .authenticate(new UsernamePasswordAuthenticationToken(userData.getUsername(),
    user.getPassword()));

    if (authentication.isAuthenticated()) {
    if (userData.getIsAuthorized().equals(false)) {
    map.put("message", "User not authorized");
    return new ResponseEntity<>(map, HttpStatus.NOT_ACCEPTABLE);
    }

    String token = jwtService.generateToken(userData.getUsername());

    var userDto = new UserDTO(userData.getId(), userData.getFullname(),
    userData.getFullname(),
    userData.getEmail(), userData.getMobile());  
    map.put("role", userData.getRole());
    map.put("partnerId", userData.getPartner().getId());
    map.put("user", userDto);
    map.put("partnerName", userData.getPartner().getName());
    map.put("token", token);
    }
    return new ResponseEntity<>(map, HttpStatus.OK);
    } catch (Exception e) {
    map.put("message", "Invalid credentials");
    return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }
    }

    // fetch partner config
    @GetMapping("/user/partnerConfig/{partnerId}")
    public ResponseEntity<?> getPartnerConfig(@PathVariable("partnerId") Long partnerId) {
        Optional<Partner> res = this.partnerRepo.findById(partnerId);
         System.out.println(res);
        if (res == null) {
            throw new ResourceNotFound("Partner did not exists");
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // fetch partner config
    @GetMapping("/admin/getAdmin")
    public ResponseEntity<?> getAdmin() {
        return ResponseEntity.status(HttpStatus.OK).body("Admin routes");
    }

    @GetMapping("/partner/getPartner")
    public ResponseEntity<?> getPartner() {
        return ResponseEntity.status(HttpStatus.OK).body("Partner routes");
    }

    @GetMapping("/user/getUser")
    public ResponseEntity<?> getUser() {
        return ResponseEntity.status(HttpStatus.OK).body("User routes");
    }

}
