package kaif.finchesterjava.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kaif.finchesterjava.entities.Bank;
import kaif.finchesterjava.entities.Loan;
import kaif.finchesterjava.entities.Partner;
import kaif.finchesterjava.entities.Profile;
import kaif.finchesterjava.entities.User;
import kaif.finchesterjava.exceptions.ResourceNotFound;
import kaif.finchesterjava.repositories.ProfileRepo;
import kaif.finchesterjava.repositories.UserRepo;
import kaif.finchesterjava.utilities.Utils;

@Service
public class ProfileService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private ProfileRepo profileRepo;

	@Autowired
	private Utils utils;

	public Profile saveProfileDetails(Profile profile, Long partnerId, Long userId) {

		Partner partner = this.utils.isPartnerExists(partnerId);
		User user = this.utils.isUserExists(userId);

		if (profile.getId() != null) {
			Profile existingProfileDetails = this.profileRepo.findById(profile.getId()).get();
			if (existingProfileDetails == null) {
				throw new ResourceNotFound("Profile details did not exists");
			}
			List<Loan> loans = this.profileRepo.findById(profile.getId()).get().getLoan();
			Bank bank = this.profileRepo.findById(profile.getId()).get().getBank();
			profile.setBank(bank);
			profile.setLoan(loans);
		}

		profile.setPartner(partner);
		profile.setUser(user);
		return profileRepo.save(profile);
	}

	public User fetchLoggedinUser(String username) {
		return this.userRepo.findByUsername(username).get();
	}

}
