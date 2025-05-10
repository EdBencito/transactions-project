package dev.ed.transaction_service.repository;

import dev.ed.transaction_service.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query("SELECT t FROM Transaction t WHERE " +
            "(:accountId IS NULL OR t.accountId = :accountId) AND " +
            "(:transactionStatus IS NULL OR t.transactionStatus = :transactionStatus) AND " +
            "(:creationDateTime IS NULL OR t.creationDateTime = :creationDateTime) AND " +
            "(:lastUpdated IS NULL OR t.lastUpdated = :lastUpdated)")
    List<Transaction> findByFilters(
            @Param("accountId") UUID accountId,
            @Param("transactionStatus") Transaction.TransactionStatus transactionStatus,
            @Param("creationDateTime") LocalDateTime creationDatetime,
            @Param("lastUpdated") LocalDateTime lastUpdated
    );

    List<Transaction> findAllByAccountId(@Param("accountId") UUID accountId);
}
