package kaif.finchesterjava.service;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import kaif.finchesterjava.dto.LoanWithProfile;
import kaif.finchesterjava.entities.Loan;
import kaif.finchesterjava.entities.Profile;
import kaif.finchesterjava.exceptions.ResourceNotFound;
import kaif.finchesterjava.repositories.LoanRepo;
import kaif.finchesterjava.repositories.ProfileRepo;

@Service
public class UserLoanService {

	@Autowired
	private LoanRepo loanRepo;

	@Autowired
	private ProfileRepo profileRepo;

	public Map<String, Object> defaultTableLoans(Long userId, Date startDate, Date endDate, Pageable pageable) {
		Map<String, Object> map = new HashMap<>();

		Long count = this.loanRepo.countByUserIdAndDateBetween(userId,
				startDate, endDate);

		Page<Loan> loans = this.loanRepo.findByUserIdAndDateBetween(userId, startDate, endDate,
				pageable);

		List<LoanWithProfile> loansWithProfileDetails = new ArrayList<>();

		for (Loan loan : loans) {
			LoanWithProfile loanWithProfile = new LoanWithProfile();
			loanWithProfile.setLoan(loan);
			loanWithProfile.setProfileEmail(loan.getProfile().getEmail());
			loanWithProfile.setProfileMobile(loan.getProfile().getMobile());
			loansWithProfileDetails.add(loanWithProfile);
		}
		map.put("loans", loansWithProfileDetails);
		map.put("count", count);
		return map;
	}

	public Map<String, Object> fetchLoanByStatusAndDate(Long userId, String search, Date startDate, Date endDate,
			Pageable pageable) {
		Map<String, Object> map = new HashMap<>();
		if (search.equalsIgnoreCase("Submitted") || search.equalsIgnoreCase("Draft")
				|| search.equalsIgnoreCase("Rejected") || search.equalsIgnoreCase("InComplete")
				|| search.equalsIgnoreCase("Appproved") || search.equalsIgnoreCase("Complete")) {

			Long count = this.loanRepo.countByUserIdAndStatusAndDateBetween(userId, search, startDate,
					endDate);

			Page<Loan> loans = this.loanRepo.findByUserIdAndStatusAndDateBetween(userId, search,
					startDate, endDate, pageable);

			List<LoanWithProfile> loansWithProfileDetails = new ArrayList<>();

			for (Loan loan : loans) {
				LoanWithProfile loanWithProfile = new LoanWithProfile();
				loanWithProfile.setLoan(loan);
				loanWithProfile.setProfileEmail(loan.getProfile().getEmail());
				loanWithProfile.setProfileMobile(loan.getProfile().getMobile());
				loansWithProfileDetails.add(loanWithProfile);
			}

			map.put("loans", loansWithProfileDetails);
			map.put("count", count);
			return map;
		}
		return map;
	}

	public Map<String, Object> fetchLoansByMobileAndDate(Long userId, Long search, Date startDate, Date endDate,
			Pageable pageable) {

		Map<String, Object> map = new HashMap<>();
		Profile profile = this.profileRepo.findByMobile(search);
		if (profile == null) {
			throw new ResourceNotFound("Mobile number did not exits");
		}

		Long count = this.loanRepo.countByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
		Page<Loan> loans = loanRepo.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate, pageable);

		List<LoanWithProfile> loansWithProfileDetails = new ArrayList<>();
		for (Loan loan : loans) {
			LoanWithProfile loanWithProfile = new LoanWithProfile();
			loanWithProfile.setLoan(loan);
			loanWithProfile.setProfileEmail(loan.getProfile().getEmail());
			loanWithProfile.setProfileMobile(loan.getProfile().getMobile());
			loansWithProfileDetails.add(loanWithProfile);
		}

		map.put("loans", loansWithProfileDetails);
		map.put("count", count);
		return map;
	}

}
