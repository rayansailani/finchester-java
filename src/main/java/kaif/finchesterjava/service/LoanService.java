package kaif.finchesterjava.service;

import java.util.UUID;
import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import kaif.finchesterjava.entities.Loan;
import kaif.finchesterjava.repositories.LoanRepo;
import kaif.finchesterjava.utilities.Utils;

@Service
public class LoanService {

	@Autowired
	private Utils utils;

	@Autowired
	private LoanRepo loanRepo;

	public Map<String, Object> fetchLoanByLoanId(UUID loanId) {
		Map<String, Object> map = new HashMap<>();
		var loanDetail = loanRepo.findAll()
				.stream()
				.filter(ele -> ele.getLoanId().equals(loanId))
				.findFirst()
				.orElse(null);

		if (loanDetail != null) {
			var profileDetail = loanDetail.getProfile();

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

			map.put("profileDetail", objectMapper.convertValue(profileDetail, Map.class));
			map.put("bankDetail", objectMapper.convertValue(profileDetail.getBank(), Map.class));
			map.put("loanDetail", loanDetail);

		}
		return map;
	}

	public Loan createLoan(Long partnerId, Long userId, Long profileId, Loan loan) {
		var partner = this.utils.isPartnerExists(partnerId);
		var user = this.utils.isUserExists(userId);
		var profile = this.utils.isProfileExists(profileId);

		if (loan.getId() == null) {
			loan.setLoanId(UUID.randomUUID());
		} else {
			loan.setId(loan.getId());
			loan.setLoanId(loan.getLoanId());
		}

		loan.setDate(new Date());
		loan.setPartner(partner);
		loan.setUser(user);
		loan.setProfile(profile);
		return this.loanRepo.save(loan);
	}

}
