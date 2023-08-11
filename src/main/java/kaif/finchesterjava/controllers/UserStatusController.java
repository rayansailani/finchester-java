package kaif.finchesterjava.controllers;

import java.util.HashMap;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import kaif.finchesterjava.service.UserLoansStatusService;
import kaif.finchesterjava.utilities.Utils;

@RestController
@RequestMapping("/user")
public class UserStatusController {

    @Autowired
    private Utils utils;

    @Autowired
    private UserLoansStatusService userLoansStatusService;

    @GetMapping("/loan/{userId}/statuses")
    public ResponseEntity<?> getStatuses(
            @PathVariable("userId") Long userId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        Map<String, Object> map = new HashMap<>();
        if (!search.isEmpty()) {
            if (utils.isValidLongValue(search)) {

                map = userLoansStatusService.fetchLoanCountStatusByMobileAndDate(
                        Long.parseLong((search)),
                        startDate, endDate);
                return ResponseEntity.status(HttpStatus.OK).body(map);
            }
            map = userLoansStatusService.fetchCountByStatusAndDate(userId, search,
                    startDate, endDate);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }

        map = this.userLoansStatusService.defaultLoanStatusesCount(userId, startDate, endDate);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

}