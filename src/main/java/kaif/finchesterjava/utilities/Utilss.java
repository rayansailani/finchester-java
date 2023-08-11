package kaif.finchesterjava.utilities;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import kaif.finchesterjava.entities.Partner;
import kaif.finchesterjava.entities.User;
import kaif.finchesterjava.exceptions.FieldAlreadyExist;
import kaif.finchesterjava.repositories.PartnerRepo;
import kaif.finchesterjava.repositories.UserRepo;

@Service
public class Utilss {


    @Autowired
    private PartnerRepo partnerRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
	private PasswordEncoder passwordEncoder;

    // signup or Update User
    public User signupUser(User user) {
        if (user.getId() != null) {
          user.setPassword(passwordEncoder.encode(user.getPassword()));
           return userRepo.save(user); 
        } else {
          user.setPassword(passwordEncoder.encode(user.getPassword()));
          return userRepo.save(user);
        }
    }

}
