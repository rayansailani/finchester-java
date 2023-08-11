package kaif.finchesterjava.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kaif.finchesterjava.entities.Loan;
import kaif.finchesterjava.exceptions.DataNotSaved;
import kaif.finchesterjava.exceptions.ResourceNotFound;
import kaif.finchesterjava.repositories.LoanRepo;
import kaif.finchesterjava.repositories.ProfileRepo;
import kaif.finchesterjava.service.LoanService;
import kaif.finchesterjava.service.UserLoanService;
import kaif.finchesterjava.utilities.Utils;

@RestController
@RequestMapping("/user")
public class LoanController {

    @Autowired
    private ProfileRepo profileRepo;

    @Autowired
    private LoanRepo loanRepo;

    @Autowired
    private LoanService loanService;

    @Autowired
    private Utils utils;

    @Autowired
    private UserLoanService userLoanService;

    @PostMapping("/loan/{partnerId}/{userId}/create-loan/{profileId}")
    public ResponseEntity<?> createLoan(
            @PathVariable("partnerId") Long partnerId,
            @PathVariable("userId") Long userId,
            @PathVariable("profileId") Long profileId,
            @RequestBody Loan loan) {
        List<Loan> profileLoans = this.loanRepo.findByProfileId(profileId);

        if (loan.getLoanAmount() < 25000) {
            return new ResponseEntity<>("We dont provide loan for less than 25000",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (loan.getLoanAmount() > 500000) {
            return new ResponseEntity<>("We provide loan upto 5 lakhs",
                    HttpStatus.NOT_ACCEPTABLE);
        }
        if (loan.getId() != null) {
            String status = this.profileRepo.findById(profileId).get().getLoan().stream()
                    .filter(ele -> ele.getId() == loan.getId()).findFirst().get().getStatus();

            if (status.equals("Submitted") || status.equals("Rejected") ||
                    status.equals("Approved")
                    || status.equals("Completed")) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body("Cannot update loan details");
            }
            var res = loanService.createLoan(partnerId, userId, profileId, loan);
            if (res == null) {
                throw new DataNotSaved("Loan details not updated");
            }
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } else {
            if (profileLoans != null) {
                for (Loan ele : profileLoans) {
                    if (ele.getIsLoanActive()) {
                        return new ResponseEntity<>("Active loan found", HttpStatus.NOT_ACCEPTABLE);
                    }
                }
            }
            var res = loanService.createLoan(partnerId, userId, profileId, loan);
            if (res == null) {
                throw new DataNotSaved("Loan not created");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }
    }

    @GetMapping("/loans/{userId}")
    public ResponseEntity<?> fetchUserLoans(
            @PathVariable("userId") Long userId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false, value = "page", defaultValue = "0") Integer page,
            @RequestParam(required = false, value = "size", defaultValue = "5") Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Map<String, Object> map = new HashMap<>();
        if (!search.isEmpty()) {
            if (utils.isValidLongValue(search)) {
                map = this.userLoanService.fetchLoansByMobileAndDate(userId,
                        Long.parseLong(search), startDate,
                        endDate, pageable);

                return ResponseEntity.status(HttpStatus.OK).body(map);
            }
            map = this.userLoanService.fetchLoanByStatusAndDate(userId, search,
                    startDate, endDate, pageable);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }
        map = userLoanService.defaultTableLoans(userId, startDate, endDate, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @GetMapping("/loan/{loanId}")
    public ResponseEntity<?> fetchLoanByLoanId(
            @PathVariable("loanId") String loanId) {

        if (loanId == null || loanId.isEmpty()) {
            return ResponseEntity.badRequest().body("Loan Id cannot be null or empty.");
        }
        try {
            UUID uuid = UUID.fromString(loanId);
            var res = loanService.fetchLoanByLoanId(uuid);
            if (res == null) {
                throw new ResourceNotFound("Loan details not available");
            }
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid Loan id: " + loanId);
        }
    }

}