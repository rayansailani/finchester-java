package kaif.finchesterjava.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kaif.finchesterjava.entities.Bank;
import kaif.finchesterjava.entities.Loan;
import kaif.finchesterjava.entities.Profile;
import kaif.finchesterjava.exceptions.ResourceNotFound;
import kaif.finchesterjava.repositories.ProfileRepo;
import kaif.finchesterjava.service.ProfileService;
import kaif.finchesterjava.utilities.Utils;

@RestController
@RequestMapping("/user")
public class ProfileController {

    @Autowired
    private Utils utils;

    @Autowired
    private ProfileRepo profileRepo;

    @Autowired
    private ProfileService profileService;

    // create or update ProfileDetails
    @PostMapping("/save-profile/{partnerId}/{userId}")
    public ResponseEntity<?> saveProfileDetails(@PathVariable("partnerId") Long partnerId,
            @PathVariable("userId") Long userId, @RequestBody Profile profile) {
        String email = profile.getEmail();
        Long mobile = profile.getMobile();

        if (mobile == null || mobile == 0L) {
            throw new ResourceNotFound("Mobile is required");
        }
        if (email == null || email.isEmpty()) {
            throw new ResourceNotFound("Email is required");
        }

        if (profile.getId() != null) {
            this.utils.isProfileEmailExists(profile.getId(), profile);
            this.utils.isProfileMobileExists(profile.getId(), profile);
           
            var res = profileService.saveProfileDetails(profile, partnerId, userId);
            if (res == null) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Profile details not updated");
            }
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } else {
            this.utils.isProfileEmailExists(null, profile);
            this.utils.isProfileMobileExists(null, profile);

            var res = profileService.saveProfileDetails(profile, partnerId, userId);
            if (res == null) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Profile details did not saved");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }

    }

    @GetMapping("/searchProfile/{mobile}")
    public ResponseEntity<?> fetchProfileByMobile(@PathVariable Long mobile) {
        Profile profileDetail = this.profileRepo.findByMobile(mobile);
        if (profileDetail == null) {
            throw new ResourceNotFound("Mobile number did not exists");
        }
        Bank bankDetail = profileDetail.getBank();
        Optional<Loan> loan = profileDetail.getLoan().stream().filter(ele -> ele.getIsLoanActive()).findFirst();
        Map<String, Object> map = new HashMap<>();
        map.put("profileDetail", profileDetail);
        map.put("bankDetail", bankDetail);
        map.put("activeLoan", loan);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

}