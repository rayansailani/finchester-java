package kaif.finchesterjava.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import kaif.finchesterjava.entities.Profile;

public interface ProfileRepo extends JpaRepository<Profile, Long> {


    Profile findByMobile(Long mobile);

    Profile findByEmail(String email);

}
