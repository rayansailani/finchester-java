package kaif.finchesterjava.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kaif.finchesterjava.exceptions.ResourceNotFound;
import kaif.finchesterjava.repositories.LoanRepo;
import kaif.finchesterjava.repositories.ProfileRepo;

@Service
public class UserLoansStatusService {

	@Autowired
	private LoanRepo loanRepo;

	@Autowired
	private ProfileRepo profileRepo;

	public Map<String, Object> fetchLoanCountStatusByMobileAndDate(Long search, Date startDate,
			Date endDate) {
		Map<String, Object> map = new HashMap<>();

		var profile = this.profileRepo.findByMobile(search);
		Long count = 0L;

		if (profile == null) {
			throw new ResourceNotFound("Mobile number did not exits");
		}
		if (search != null && startDate == null && endDate == null) {
			var size = this.loanRepo.findByProfileId(profile.getId()).size();
			count = Long.valueOf(size);
		}
		if (search != null && startDate != null && endDate != null) {
			var size = this.loanRepo.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate).size();
			count = Long.valueOf(size);
		}
		map.put("Loans For this Mobile in given date range", count);
		return map;
	}

	public Map<String, Object> fetchCountByStatusAndDate(Long userId, String search, Date startDate,
			Date endDate) {
		Map<String, Object> map = new HashMap<>();

		if (search.equalsIgnoreCase("Submitted") || search.equalsIgnoreCase("Draft")
				|| search.equalsIgnoreCase("Rejected") ||
				search.equalsIgnoreCase("InComplete")
				|| search.equalsIgnoreCase("Appproved") ||
				search.equalsIgnoreCase("Complete")) {

			if (search != null && startDate != null && endDate != null) {
				Long searchedStatusCount = this.loanRepo.countByUserIdAndStatusAndDateBetween(userId, search,
						startDate,
						endDate);
				map.put(search, searchedStatusCount);
				return map;
			} else {
				throw new ResourceNotFound("status & dates are required");
			}
		} else {
			throw new ResourceNotFound("Invalid search");
		}

	}

	public Map<String, Object> defaultLoanStatusesCount(Long userId, Date startDate, Date endDate) {
		Map<String, Object> map = new HashMap<>();
		Long totalLoansCount = this.loanRepo.countByUserIdAndDateBetween(userId,
				startDate, endDate);
		Long submittedCount = this.loanRepo.countByUserIdAndStatusAndDateBetween(userId, "Submitted",
				startDate,
				endDate);
		Long draftCount = this.loanRepo.countByUserIdAndStatusAndDateBetween(userId, "Draft", startDate,
				endDate);
		Long approvedCount = this.loanRepo.countByUserIdAndStatusAndDateBetween(userId, "Approved", startDate,
				endDate);
		Long inCompleteCount = this.loanRepo.countByUserIdAndStatusAndDateBetween(userId, "InComplete",
				startDate,
				endDate);
		Long rejectedCount = this.loanRepo.countByUserIdAndStatusAndDateBetween(userId, "Rejected", startDate,
				endDate);
		Long completedCount = this.loanRepo.countByUserIdAndStatusAndDateBetween(userId, "Completed",
				startDate,
				endDate);

		map.put("Loans", totalLoansCount);
		map.put("Submitted", submittedCount);
		map.put("Draft", draftCount);
		map.put("Approved", approvedCount);
		map.put("InComplete", inCompleteCount);
		map.put("Rejected", rejectedCount);
		map.put("Completed", completedCount);
		return map;
	}

}
