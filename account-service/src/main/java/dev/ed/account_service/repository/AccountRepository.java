package dev.ed.account_service.repository;

import dev.ed.account_service.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    boolean existsByAccountNumber(String accountNumber);

    @Query(value = "SELECT a.accountId FROM Account a ORDER BY RANDOM() LIMIT 1")
    Optional<UUID> findRandomAccountId();

}

