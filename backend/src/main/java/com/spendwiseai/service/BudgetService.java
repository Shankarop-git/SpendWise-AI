package com.spendwiseai.service;

import com.spendwiseai.dto.BudgetDto;
import com.spendwiseai.entity.Budget;
import com.spendwiseai.entity.Transaction;
import com.spendwiseai.entity.User;
import com.spendwiseai.repository.BudgetRepository;
import com.spendwiseai.repository.TransactionRepository;
import com.spendwiseai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Transactional
    public BudgetDto.Response createOrUpdateBudget(Long userId, BudgetDto.Request request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Optional<Budget> existing = budgetRepository.findByUserIdAndCategoryAndMonthAndYear(
                userId, request.getCategory(), request.getMonth(), request.getYear());

        Budget budget;
        if (existing.isPresent()) {
            budget = existing.get();
            budget.setMonthlyLimit(request.getMonthlyLimit());
        } else {
            budget = Budget.builder()
                    .user(user)
                    .category(request.getCategory())
                    .monthlyLimit(request.getMonthlyLimit())
                    .month(request.getMonth())
                    .year(request.getYear())
                    .build();
        }

        Budget saved = budgetRepository.save(budget);
        return mapToResponse(saved, userId);
    }

    public List<BudgetDto.Response> getBudgets(Long userId, Integer month, Integer year) {
        LocalDate now = LocalDate.now();
        int m = month != null ? month : now.getMonthValue();
        int y = year != null ? year : now.getYear();

        List<Budget> budgets = budgetRepository.findByUserIdAndMonthAndYear(userId, m, y);
        return budgets.stream().map(b -> mapToResponse(b, userId)).toList();
    }

    public BudgetDto.Response getBudgetProgress(Long userId, Long budgetId) {
        Budget budget = budgetRepository.findByIdAndUserId(budgetId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Budget not found or access denied"));
        return mapToResponse(budget, userId);
    }

    @Transactional
    public void deleteBudget(Long userId, Long budgetId) {
        Budget budget = budgetRepository.findByIdAndUserId(budgetId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Budget not found or access denied"));
        budgetRepository.delete(budget);
    }

    private BudgetDto.Response mapToResponse(Budget b, Long userId) {
        YearMonth ym = YearMonth.of(b.getYear(), b.getMonth());
        LocalDate startDate = ym.atDay(1);
        LocalDate endDate = ym.atEndOfMonth();

        BigDecimal spent = transactionRepository.sumExpenseByUserIdAndCategoryAndDateBetween(
                userId, b.getCategory(), startDate, endDate);
        if (spent == null) spent = BigDecimal.ZERO;

        BigDecimal remaining = b.getMonthlyLimit().subtract(spent);
        BigDecimal percentageUsed = BigDecimal.ZERO;

        if (b.getMonthlyLimit().compareTo(BigDecimal.ZERO) > 0) {
            percentageUsed = spent.multiply(BigDecimal.valueOf(100))
                    .divide(b.getMonthlyLimit(), 2, RoundingMode.HALF_UP);
        }

        BudgetDto.Status status;
        double pct = percentageUsed.doubleValue();
        if (pct > 100) {
            status = BudgetDto.Status.EXCEEDED;
        } else if (pct >= 90) {
            status = BudgetDto.Status.ALERT;
        } else if (pct >= 70) {
            status = BudgetDto.Status.WARNING;
        } else {
            status = BudgetDto.Status.NORMAL;
        }

        return BudgetDto.Response.builder()
                .id(b.getId())
                .category(b.getCategory())
                .monthlyLimit(b.getMonthlyLimit())
                .spent(spent)
                .remaining(remaining)
                .percentageUsed(percentageUsed)
                .status(status)
                .month(b.getMonth())
                .year(b.getYear())
                .build();
    }
}
