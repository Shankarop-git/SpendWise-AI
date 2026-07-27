package com.spendwiseai.repository;

import com.spendwiseai.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId " +
           "AND (:type IS NULL OR t.type = :type) " +
           "AND (:category IS NULL OR t.category = :category) " +
           "AND (:startDate IS NULL OR t.date >= :startDate) " +
           "AND (:endDate IS NULL OR t.date <= :endDate) " +
           "AND (:search IS NULL OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Transaction> findFiltered(
            @Param("userId") Long userId,
            @Param("type") Transaction.TransactionType type,
            @Param("category") Transaction.Category category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("search") String search,
            Pageable pageable
    );

    List<Transaction> findByUserIdAndDateBetweenOrderByDateDesc(Long userId, LocalDate startDate, LocalDate endDate);

    List<Transaction> findTop10ByUserIdOrderByDateDescIdDesc(Long userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.type = :type AND t.date BETWEEN :startDate AND :endDate")
    BigDecimal sumAmountByUserIdAndTypeAndDateBetween(
            @Param("userId") Long userId,
            @Param("type") Transaction.TransactionType type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.user.id = :userId AND t.type = 'EXPENSE' AND t.category = :category AND t.date BETWEEN :startDate AND :endDate")
    BigDecimal sumExpenseByUserIdAndCategoryAndDateBetween(
            @Param("userId") Long userId,
            @Param("category") Transaction.Category category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
