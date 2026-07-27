package com.spendwiseai.repository;

import com.spendwiseai.entity.Budget;
import com.spendwiseai.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByIdAndUserId(Long id, Long userId);

    List<Budget> findByUserIdAndMonthAndYear(Long userId, Integer month, Integer year);

    Optional<Budget> findByUserIdAndCategoryAndMonthAndYear(Long userId, Transaction.Category category, Integer month, Integer year);
}
