package kaif.finchesterjava.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kaif.finchesterjava.entities.Partner;
import kaif.finchesterjava.entities.User;
import kaif.finchesterjava.exceptions.ResourceNotFound;
import kaif.finchesterjava.repositories.LoanRepo;
import kaif.finchesterjava.service.AdminService;
import kaif.finchesterjava.utilities.PartnerUtils;
import kaif.finchesterjava.utilities.UniqueUtils;
import kaif.finchesterjava.utilities.Utils;
import kaif.finchesterjava.utilities.Utilss;

@RequestMapping("/admin")
@RestController
public class AdminController {

    @Autowired
    private Utils utils;

    @Autowired
    private AdminService adminService;

    @Autowired
    private LoanRepo loanRepo;

    // create or update Partner
    @PostMapping("/create-partner")
    public ResponseEntity<?> savePartner(@RequestBody Partner partner) {
        if (partner.getName() == null || partner.getMobile() == null ||
                partner.getEmail() == null) {
            throw new ResourceNotFound("Name, email, mobile, username, password, isAuthorized  are required fields");
        }
        adminService.checkPartnerUniqueFields(partner);
        if (partner.getId() != null) {
            var res = adminService.savePartner(partner);
            if (res == null) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Partner did not saved");
            }
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } else {
            var res = adminService.savePartner(partner);
            if (res == null) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Partner did not saved");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }
    }

    // update Partner config
    @PostMapping("/update-config")
    public ResponseEntity<?> savePartnerConfig(@RequestBody Partner partner) {
        // if(partner.getId() == null){
        // throw new ResourceNotFound("Partner did not exists");
        // }
        var res = adminService.savePartnerConfig(partner);
        if (res == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Partner config did not saved");
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


    // **************** admin fetch a partner ****************
    // @GetMapping("/partner/{id}")
    // public ResponseEntity<?> fetchPartner(@PathVariable("id") Long id) {
    //     var res = adminService.fetchPartner(id);
    //     if (res == null) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No partner");
    //     }
    //     return ResponseEntity.status(HttpStatus.OK).body(res);
    // }

    // **************** admin fetch all partners ****************
    @GetMapping("/partners")
    public ResponseEntity<?> fetchPartners() {
        var res = adminService.fetchPartners();
        if (res == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No partners");
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // **************** partner signup ****************
    @PostMapping("/partner-signup/{partnerId}")
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
            var res = adminService.partnerSignup(partnerId, user);
            if (res == null) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Partner signup details not updated");
            }
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } else {
            var res = adminService.partnerSignup(partnerId, user);
            if (res == null) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Partner not signed up");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }
    }

    // **************** fetch partner signup detail ****************
    @GetMapping("/partner-signup-detail/{id}")
    public ResponseEntity<?> fetchPartnerSignupDetail(@PathVariable("id") Long id) {
        var res = adminService.fetchPartnerSignupDetail(id);
        if (res == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Partner  not available");
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    // Update Loan Status
    @PutMapping("/loans/{id}/{status}")
    public ResponseEntity<?> updateLoanStatus(
            @PathVariable("id") Long id,
            @PathVariable("status") String status) {
        // loanRepo.deleteAll();
        // profileRepo.deleteAll();
        // bankRepo.deleteAll();
        // return ResponseEntity.status(HttpStatus.OK).body("deleted");
        var loan = loanRepo.findById(id).get();
        loan.setStatus(status);
        return ResponseEntity.status(HttpStatus.OK).body(this.loanRepo.save(loan));
    }

}