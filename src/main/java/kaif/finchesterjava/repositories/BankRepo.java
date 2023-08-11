package kaif.finchesterjava.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import kaif.finchesterjava.entities.Bank;

public interface BankRepo extends JpaRepository<Bank, Long> {

}
