package kaif.finchesterjava.utilities;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import kaif.finchesterjava.entities.Partner;
import kaif.finchesterjava.exceptions.FieldAlreadyExist;
import kaif.finchesterjava.repositories.PartnerRepo;

@Service
public class PartnerUtils {


    @Autowired
    private PartnerRepo partnerRepo;

    // check partner name
    public void checkPartnerName(Partner partner) {
        if (partner.getId() != null) {
            List<Partner> partnerList = partnerRepo.findAll();
            List<Partner> filteredList = partnerList.stream().filter(ele -> !ele.getId().equals(partner.getId()))
                    .collect(Collectors.toList());
            Boolean nameExists = filteredList.stream()
                    .anyMatch(ele -> ele.getName().equalsIgnoreCase(partner.getName()));

            if (nameExists) {
                throw new FieldAlreadyExist("Partner name already exists");
            }
        } else {
            var nameExists = partnerRepo.findByNameIgnoreCase(partner.getName());
            if (nameExists != null) {
                throw new FieldAlreadyExist("Partner name already exists");
            }
        }
    }

}
