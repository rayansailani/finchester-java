package kaif.finchesterjava.repositories;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import kaif.finchesterjava.entities.Loan;

public interface LoanRepo extends JpaRepository<Loan, Long> {

    Long countByUserIdAndStatusAndDateBetween(Long userId, String status, Date startDate, Date endDate);

    Long countByUserIdAndDateBetween(Long userId, Date startDate, Date endDate);

    Long countByDateBetween(Date startDate, Date endDate);

    Long countByStatusAndDateBetween(String status, Date startDate, Date endDate);

    Long countByProfileIdAndDateBetween(Long profileId, Date startDate, Date endDate);

    Page<Loan> findByUserIdAndDateBetween(Long userId, Date startDate, Date endDate, Pageable pageable);

    List<Loan> findByProfileIdAndDateBetween(Long profileId, Date startDate, Date endDate);



    List<Loan> findByProfileId(Long profileId);

    List<Loan> findByDateBetween(Date startDate, Date endDate);

    Page<Loan> findByProfileIdAndDateBetween(Long profileId, Date startDate, Date endDate, Pageable pageable);

    Page<Loan> findByDateBetween(Date startDate, Date endDate, Pageable pageable);

    Page<Loan> findByProfileIdAndStatusAndDateBetween(Long profileId, String search, Date startDate, Date endDate,
            Pageable pageable);

    Page<Loan> findByUserIdAndStatusAndDateBetween(Long userId, String search, Date startDate, Date endDate,
            Pageable pageable);


}
