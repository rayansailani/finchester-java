package kaif.finchesterjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kaif.finchesterjava.entities.User;
import kaif.finchesterjava.repositories.PartnerRepo;
import kaif.finchesterjava.repositories.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class PartnerService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PartnerRepo partnerRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User userSignup(Long partnerId, User user) {
		var partner = partnerRepo.findById(partnerId).get();

		if (user.getId() != null) {
			user.setId(user.getId());
		}
		user.setPartner(partner);
		user.setRole("ROLE_USER");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepo.save(user);
	}

}
