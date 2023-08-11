package kaif.finchesterjava.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import kaif.finchesterjava.entities.Partner;

public interface PartnerRepo extends JpaRepository<Partner, Long> {
    Partner findByNameIgnoreCase(String name);

    Partner findByMobile(Long mobile);

    Partner findByEmailIgnoreCase(String email);
}
