package kaif.finchesterjava.utilities;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kaif.finchesterjava.entities.User;
import kaif.finchesterjava.exceptions.FieldAlreadyExist;
import kaif.finchesterjava.repositories.UserRepo;

@Service
public class UniqueUtils {

    @Autowired
    private UserRepo userRepo;

    // check email and mobile
    public void checkUniqueFields(String email, Long mobile) {

        var mobileExists = userRepo.findByMobile(mobile);
        var emailExists = userRepo.findByEmailIgnoreCase(email);

        if (mobileExists != null) {
            throw new FieldAlreadyExist("Mobile already exists");
        }
        if (emailExists != null) {
            throw new FieldAlreadyExist("Email already exists");
        }
    }

}
