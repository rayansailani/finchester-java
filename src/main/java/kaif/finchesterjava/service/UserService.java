package kaif.finchesterjava.service;

import java.util.stream.Collectors;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kaif.finchesterjava.entities.Partner;
import kaif.finchesterjava.entities.User;
import kaif.finchesterjava.exceptions.FieldAlreadyExist;
import kaif.finchesterjava.repositories.PartnerRepo;
import kaif.finchesterjava.repositories.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PartnerRepo partnerRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User saveUser(User user, Long partnerId) {
		Partner partner = this.partnerRepo.findById(partnerId).get();

		if (user.getId() != null) {		

			List<User> userList = userRepo.findAll();
			List<User> filteredList = userList.stream().filter(ele -> !ele.getId().equals(user.getId()))
					.collect(Collectors.toList());

			Boolean mobileExists = filteredList.stream().anyMatch(ele -> ele.getMobile().equals(user.getMobile()));
			Boolean emailExists = filteredList.stream().anyMatch(ele -> ele.getEmail().equals(user.getEmail()));
			
			if (mobileExists) {
				throw new FieldAlreadyExist("Mobile already exists");
			}
			if (emailExists) {
				throw new FieldAlreadyExist("Email already exists");
			}

			user.setId(user.getId());

		} else {

			User emailExists = userRepo.findByEmailIgnoreCase(user.getEmail());
			User mobileExists = userRepo.findByMobile(user.getMobile());

			if (mobileExists != null) {
				throw new FieldAlreadyExist("Mobile already exits");
			}
			if (emailExists != null) {
				throw new FieldAlreadyExist("Email already exits");
			}

		}
		user.setPartner(partner);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepo.save(user);
	}

	public User fetchLoggedinUser(String username) {
		return this.userRepo.findByUsername(username).get();
	}

}
