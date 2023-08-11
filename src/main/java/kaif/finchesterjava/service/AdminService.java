package kaif.finchesterjava.service;

import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kaif.finchesterjava.entities.Partner;
import kaif.finchesterjava.entities.User;
import kaif.finchesterjava.exceptions.FieldAlreadyExist;
import kaif.finchesterjava.exceptions.ResourceNotFound;
import kaif.finchesterjava.repositories.PartnerRepo;
import kaif.finchesterjava.repositories.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AdminService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PartnerRepo partnerRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public void checkPartnerUniqueFields(Partner partner) {
		
		if (partner.getId() != null) {
			List<Partner> partnerList = partnerRepo.findAll();
			List<Partner> filteredList = partnerList.stream().filter(ele -> !ele.getId().equals(partner.getId()))
					.collect(Collectors.toList());
			Boolean nameExists = filteredList.stream()
					.anyMatch(ele -> ele.getName().equalsIgnoreCase(partner.getName()));
			Boolean mobileExists = filteredList.stream().anyMatch(ele -> ele.getMobile().equals(partner.getMobile()));
			Boolean emailExists = filteredList.stream()
					.anyMatch(ele -> ele.getEmail().equalsIgnoreCase(partner.getEmail()));

			if (nameExists) {
				throw new FieldAlreadyExist("Partner name already exists");
			}
			if (mobileExists) {
				throw new FieldAlreadyExist("Mobile already exists");
			}
			if (emailExists) {
				throw new FieldAlreadyExist("Email already exists");
			}

		} else {
			Partner nameExists = partnerRepo.findByNameIgnoreCase(partner.getName());
			Partner mobileExists = partnerRepo.findByMobile(partner.getMobile());
			Partner emailExists = partnerRepo.findByEmailIgnoreCase(partner.getEmail());
			if (nameExists != null) {
				throw new FieldAlreadyExist("Partner name already exists");
			}
			if (mobileExists != null) {
				throw new FieldAlreadyExist("Mobile already exists");
			}
			if (emailExists != null) {
				throw new FieldAlreadyExist("Email already exists");
			}

		}
	}

	// public void checkUniqueFields(Partner partner) {
	// if (partner.getId() != null) {
	// List<Partner> partnerList = partnerRepo.findAll();
	// List<Partner> filteredList = partnerList.stream().filter(ele ->
	// !ele.getId().equals(partner.getId()))
	// .collect(Collectors.toList());
	// Boolean nameExists = filteredList.stream()
	// .anyMatch(ele -> ele.getName().equalsIgnoreCase(partner.getName()));
	// Boolean mobileExists = filteredList.stream().anyMatch(ele ->
	// ele.getMobile().equals(partner.getMobile()));
	// Boolean emailExists = filteredList.stream()
	// .anyMatch(ele -> ele.getEmail().equalsIgnoreCase(partner.getEmail()));

	// if (nameExists) {
	// throw new FieldAlreadyExist("Partner name already exists");
	// }
	// if (mobileExists) {
	// throw new FieldAlreadyExist("Mobile already exists");
	// }
	// if (emailExists) {
	// throw new FieldAlreadyExist("Email already exists");
	// }

	// } else {
	// Partner nameExists = partnerRepo.findByNameIgnoreCase(partner.getName());
	// Partner mobileExists = partnerRepo.findByMobile(partner.getMobile());
	// Partner emailExists = partnerRepo.findByEmailIgnoreCase(partner.getEmail());
	// if (nameExists != null) {
	// throw new FieldAlreadyExist("Partner name already exists");
	// }
	// if (mobileExists != null) {
	// throw new FieldAlreadyExist("Mobile already exists");
	// }
	// if (emailExists != null) {
	// throw new FieldAlreadyExist("Email already exists");
	// }
	// }
	// }

	// public void checkUserUniqueFields(User user) {
	// if (user.getId() != null) {
	// List<User> userList = userRepo.findAll();

	// List<User> filteredList = userList.stream().filter(ele ->
	// !ele.getId().equals(user.getId()))
	// .collect(Collectors.toList());

	// Boolean usernameExists = filteredList.stream()
	// .anyMatch(ele -> ele.getUsername().equalsIgnoreCase(user.getUsername()));
	// Boolean mobileExists = filteredList.stream().anyMatch(ele ->
	// ele.getMobile().equals(user.getMobile()));
	// Boolean emailExists = filteredList.stream()
	// .anyMatch(ele -> ele.getEmail().equalsIgnoreCase(user.getEmail()));

	// if (usernameExists) {
	// throw new FieldAlreadyExist("Username already exists");
	// }

	// if (mobileExists) {
	// throw new FieldAlreadyExist("Mobile already exists");
	// }
	// if (emailExists) {
	// throw new FieldAlreadyExist("Email already exists");
	// }

	// } else {
	// var usernameExists = this.userRepo.findAll().stream()
	// .anyMatch(ele -> ele.getUsername().equalsIgnoreCase(user.getUsername()));
	// User mobileExists = this.userRepo.findByMobile(user.getMobile());

	// User emailExists = this.userRepo.findByEmailIgnoreCase(user.getEmail());
	// if (usernameExists) {
	// throw new FieldAlreadyExist("Username already exists");
	// }
	// if (mobileExists != null) {
	// throw new FieldAlreadyExist("Mobile already exists");
	// }
	// if (emailExists != null) {
	// throw new FieldAlreadyExist("Email already exists");
	// }
	// }

	// }

	public Partner savePartner(Partner partner) {
		if (partner.getId() != null) {
			Partner existingPartner = partnerRepo.findById(partner.getId()).get();
			partner.setId(partner.getId());
			partner.setName(partner.getName());
			partner.setMobile(partner.getMobile());
			partner.setEmail(partner.getEmail());
			partner.setConfig(existingPartner.getConfig());
			partner.setUser(existingPartner.getUser());
			partner.setProfile(existingPartner.getProfile());
			partner.setLoan(existingPartner.getLoan());
			partner.setBank(existingPartner.getBank());
		}

		return this.partnerRepo.save(partner);

	}

	// update Partner config
	public Partner savePartnerConfig(Partner partner) {
		Partner existingPartner = partnerRepo.findById(partner.getId()).get();
		partner.setId(existingPartner.getId());
		partner.setConfig(partner.getConfig());
		partner.setName(existingPartner.getName());
		partner.setMobile(existingPartner.getMobile());
		partner.setEmail(existingPartner.getEmail());
		partner.setUser(existingPartner.getUser());
		partner.setProfile(existingPartner.getProfile());
		partner.setLoan(existingPartner.getLoan());
		partner.setBank(existingPartner.getBank());
		return partnerRepo.save(partner);
	}

	public List<Partner> fetchPartners() {
		return partnerRepo.findAll();
	}

	public User partnerSignup(Long partnerId, User user) {
		var partner = partnerRepo.findById(partnerId).get();

		if (user.getId() != null) {
			user.setId(user.getId());
		}
		user.setRole("ROLE_PARTNER");
		user.setPartner(partner);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepo.save(user);
	}

	public Map<String, Object> fetchPartnerSignupDetail(Long id) {
		Map<String, Object> map = new HashMap<>();
		var partnerId = userRepo.findById(id).get().getPartner().getId();
		var partner = userRepo.findById(id).get();
		map.put("partner", partner);
		map.put("partnerId", partnerId);
		return map;
	}

}
