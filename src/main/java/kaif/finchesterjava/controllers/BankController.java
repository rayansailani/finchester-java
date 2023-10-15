package kaif.finchesterjava.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import kaif.finchesterjava.entities.Bank;
import kaif.finchesterjava.exceptions.DataNotSaved;
import kaif.finchesterjava.exceptions.ResourceNotFound;
import kaif.finchesterjava.service.BankService;

@RestController
@RequestMapping("/user")
public class BankController {

    @Autowired
    private BankService bankService;

    @PostMapping("/loan/{partnerId}/{userId}/create-bank/{profileId}")
    public ResponseEntity<?> createLoan(@PathVariable("partnerId") Long partnerId, @PathVariable("userId") Long userId,
            @PathVariable("profileId") Long profileId, @RequestBody Bank bank) {
        if (bank.getAadhaarNo() == null || bank.getAccountNo() == null || bank.getAccountType() == null
                || bank.getIfscCode() == null || bank.getPanNo() == null) {
            throw new ResourceNotFound(
                    "Account no, aadhaar no, pan no, ifsc code and account type are required fields");
        }
        if (bank.getId() != null) {            
            var res = bankService.createBank(partnerId, userId, profileId, bank);
            if (res == null) {
                throw new DataNotSaved("Bank details not updated");
            }
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } else {            
            var res = bankService.createBank(partnerId, userId, profileId, bank);
            if (res == null) {
                throw new DataNotSaved("Bank details could not be saved");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }
        // return ResponseEntity.status(HttpStatus.CREATED).body("res");
    }

}