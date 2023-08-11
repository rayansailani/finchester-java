package kaif.finchesterjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kaif.finchesterjava.entities.Bank;
import kaif.finchesterjava.repositories.BankRepo;
import kaif.finchesterjava.utilities.Utils;

@Service
public class BankService {

	@Autowired
	private Utils utils;

	@Autowired
	private BankRepo bankRepo;

	public Bank createBank(Long partnerId, Long userId, Long profileId, Bank bank) {
		var partner = this.utils.isPartnerExists(partnerId);
		var user = this.utils.isUserExists(userId);
		var profile = this.utils.isProfileExists(profileId);

		if (bank.getId() != null) {
			utils.isAccountExists(bank.getId(), bank);
			utils.isAadhaarExists(bank.getId(), bank);
			utils.isPanExists(bank.getId(), bank);
			bank.setId(bank.getId());
		} else {
			utils.isAccountExists(null, bank);
			utils.isAadhaarExists(null, bank);
			utils.isPanExists(null, bank);
		}

		bank.setPartner(partner);
		bank.setUser(user);
		bank.setProfile(profile);
		return this.bankRepo.save(bank);
	}

}
