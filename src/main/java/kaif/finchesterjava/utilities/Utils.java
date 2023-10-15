package kaif.finchesterjava.utilities;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kaif.finchesterjava.entities.Bank;
import kaif.finchesterjava.entities.Partner;
import kaif.finchesterjava.entities.Profile;
import kaif.finchesterjava.entities.User;
import kaif.finchesterjava.exceptions.FieldAlreadyExist;
import kaif.finchesterjava.exceptions.ResourceNotFound;
import kaif.finchesterjava.repositories.BankRepo;
import kaif.finchesterjava.repositories.PartnerRepo;
import kaif.finchesterjava.repositories.ProfileRepo;
import kaif.finchesterjava.repositories.UserRepo;

@Service
public class Utils {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PartnerRepo partnerRepo;

    @Autowired
    private ProfileRepo profileRepo;

    @Autowired
    private BankRepo bankRepo;

    public void isLongNullOrEmptyOrUndefined(Long value) {
        if (value == null) {
            throw new ResourceNotFound("value is null");
        }

        String strValue = String.valueOf(value);

        if (strValue.trim().isEmpty()) {
            throw new ResourceNotFound("value is empty");
        }
    }

    public Boolean isStringNullOrEmpty(String value) {
        return value == null || value.trim().isBlank() || value.trim().isEmpty();
    }

    public Boolean isLongNullOrEmpty(Long value) {
        return value == null;
    }

    public Boolean isBoolNullOrEmpty(Boolean value) {
        return value == null;
    }

    public Boolean isLong(String input) {
        try {
            Long.parseLong(input);
            return true; // Conversion successful
        } catch (NumberFormatException e) {
            return false; // Conversion failed
        }
    }

    public Boolean isString(String input) {
        try {
            if (input instanceof String) {
                return true;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public Partner fetchPartnerByMobileOrEmail (String username) {
        Partner partner;
        try {

            Long mobile = Long.parseLong(username);
             partner = partnerRepo.findByMobile(mobile);
        } catch (NumberFormatException e) {
            partner = partnerRepo.findByEmailIgnoreCase(username);
        }
        return partner;
    }

    public User fetchUserByMobileOrEmail(String username) {
        User user;
        try {

            Long mobile = Long.parseLong(username);
            user = userRepo.findByMobile(mobile);
        } catch (NumberFormatException e) {
            user = userRepo.findByEmailIgnoreCase(username);
        }
        return user;
    }

    String str = "12345";

    // Check if the value is numeric
    boolean isNumeric = str.matches("-?\\d+");

    // Check if the value is alphanumeric
    boolean isAlphanumeric = str.matches("[a-zA-Z0-9]+");

    // Check if the value is a valid email address
    boolean isEmailAddress = str.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");

    // Check if the value is empty or consists only of whitespace characters
    boolean isBlank = str.trim().isEmpty();

    // Check if the value is null or empty
    boolean isNullOrEmpty = str == null || str.isEmpty();

    public void checkUserUniqueFields(User user) {
        if (user.getId() != null) {
            List<User> userList = userRepo.findAll();

            List<User> filteredList = userList.stream().filter(ele -> !ele.getId().equals(user.getId()))
                    .collect(Collectors.toList());

            // Boolean usernameExists = filteredList.stream()
            //         .anyMatch(ele -> ele.getUsername().equalsIgnoreCase(user.getUsername()));
            Boolean mobileExists = filteredList.stream().anyMatch(ele -> ele.getMobile().equals(user.getMobile()));
            Boolean emailExists = filteredList.stream()
                    .anyMatch(ele -> ele.getEmail().equalsIgnoreCase(user.getEmail()));

            // if (usernameExists) {
            //     throw new FieldAlreadyExist("Username already exists");
            // }

            if (mobileExists) {
                throw new FieldAlreadyExist("Mobile already exists");
            }
            if (emailExists) {
                throw new FieldAlreadyExist("Email already exists");
            }

        } else {
            var usernameExists = this.userRepo.findAll().stream()
                    .anyMatch(ele -> ele.getUsername().equalsIgnoreCase(user.getUsername()));
            User mobileExists = this.userRepo.findByMobile(user.getMobile());

            User emailExists = this.userRepo.findByEmailIgnoreCase(user.getEmail());
            if (usernameExists) {
                throw new FieldAlreadyExist("Username already exists");
            }
            if (mobileExists != null) {
                throw new FieldAlreadyExist("Mobile already exists");
            }
            if (emailExists != null) {
                throw new FieldAlreadyExist("Email already exists");
            }
        }

    }

    public Partner isPartnerExists(Long partnerId) {
        return this.partnerRepo.findById(partnerId).orElseThrow(() -> new ResourceNotFound("Partner did not exists"));
    }

    public User isUserExists(Long userId) {
        return this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFound("Partner User did not exists"));
    }

    public Profile isProfileExists(Long profileId) {
        return this.profileRepo.findById(profileId)
                .orElseThrow(() -> new ResourceNotFound("Profile did not exists"));
    }

    public void isProfileEmailExists(Long id, Profile profile) {
        List<Profile> profileList = profileRepo.findAll();

        Boolean response = false;
        if (id == null) {
            response = profileList.stream()
                    .anyMatch(ele -> ele.getEmail().equals(profile.getEmail()));

        } else {
            List<Profile> profiles = profileList.stream()
                    .filter(ele -> !ele.getId().equals(profile.getId()))
                    .collect(Collectors.toList());

            response = profiles.stream()
                    .anyMatch(ele -> ele.getEmail().equals(profile.getEmail()));

        }
        if (response) {
            throw new FieldAlreadyExist("Email already exists");
        }
    }

    public void isProfileMobileExists(Long id, Profile profile) {
        List<Profile> profileList = profileRepo.findAll();
        Boolean response = false;
        if (id == null) {
            response = profileList.stream()
                    .anyMatch(ele -> ele.getMobile().equals(profile.getMobile()));
        } else {
            List<Profile> profiles = profileList.stream()
                    .filter(ele -> !ele.getId().equals(profile.getId()))
                    .collect(Collectors.toList());
            response = profiles.stream()
                    .anyMatch(ele -> ele.getMobile().equals(profile.getMobile()));
        }
        if (response) {
            throw new FieldAlreadyExist("Mobile number already exists");
        }
    }

    public void isAccountExists(Long id, Bank bank) {
        List<Bank> allbankDetails = this.bankRepo.findAll();
        Boolean response = false;
        if (id == null) {
            response = allbankDetails.stream()
                    .anyMatch(ele -> ele.getAccountNo().equals(bank.getAccountNo()));
        } else {
            List<Bank> bankDetails = allbankDetails.stream()
                    .filter(ele -> !ele.getId().equals(bank.getId()))
                    .collect(Collectors.toList());
            response = bankDetails.stream()
                    .anyMatch(ele -> ele.getAccountNo().equals(bank.getAccountNo()));
        }
        if (response) {
            throw new FieldAlreadyExist("Bank account already exists");
        }
    }

    public void isPanExists(Long id, Bank bank) {
        List<Bank> allbankDetails = this.bankRepo.findAll();
        Boolean response = false;
        if (id == null) {
            response = allbankDetails.stream()
                    .anyMatch(ele -> ele.getPanNo().equals(bank.getPanNo()));
        } else {
            List<Bank> bankDetails = allbankDetails.stream()
                    .filter(ele -> !ele.getId().equals(bank.getId()))
                    .collect(Collectors.toList());
            response = bankDetails.stream()
                    .anyMatch(ele -> ele.getPanNo().equals(bank.getPanNo()));
        }
        if (response) {
            throw new FieldAlreadyExist("Pan number already exists");
        }
    }

    public void isAadhaarExists(Long id, Bank bank) {
        List<Bank> allbankDetails = this.bankRepo.findAll();
        Boolean response = false;
        if (id == null) {
            response = allbankDetails.stream()
                    .anyMatch(ele -> ele.getAadhaarNo().equals(bank.getAadhaarNo()));
        } else {
            List<Bank> bankDetails = allbankDetails.stream()
                    .filter(ele -> !ele.getId().equals(bank.getId()))
                    .collect(Collectors.toList());
            response = bankDetails.stream()
                    .anyMatch(ele -> ele.getAadhaarNo().equals(bank.getAadhaarNo()));
        }
        if (response) {
            throw new FieldAlreadyExist("Aadhaar number already exists");
        }
    }

    public boolean isValidLongValue(String value) {
        try {
            Long.parseLong(value);
            return true; // Value is a valid Long value
        } catch (NumberFormatException e) {
            return false; // Value is not a valid Long value
        }
    }

}
