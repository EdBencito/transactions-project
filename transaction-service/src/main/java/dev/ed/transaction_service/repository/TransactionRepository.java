package dev.ed.transaction_service.repository;

import dev.ed.transaction_service.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findAllByAccountId(@Param("accountId") UUID accountId);

    @Query("SELECT t.accountId FROM Transaction t WHERE t.transactionId = :transactionId")
    String findAccountIdByTransactionId(@Param("transactionId") UUID transactionId);
}
