package com.spendwiseai.service;

import com.spendwiseai.dto.DashboardDto;
import com.spendwiseai.dto.TransactionDto;
import com.spendwiseai.entity.Transaction;
import com.spendwiseai.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionRepository transactionRepository;

    public DashboardDto.SummaryResponse getSummary(Long userId) {
        LocalDate now = LocalDate.now();
        YearMonth currentYm = YearMonth.from(now);
        YearMonth prevYm = currentYm.minusMonths(1);

        BigDecimal curIncome = sum(userId, Transaction.TransactionType.INCOME, currentYm.atDay(1), currentYm.atEndOfMonth());
        BigDecimal curExpenses = sum(userId, Transaction.TransactionType.EXPENSE, currentYm.atDay(1), currentYm.atEndOfMonth());

        BigDecimal prevIncome = sum(userId, Transaction.TransactionType.INCOME, prevYm.atDay(1), prevYm.atEndOfMonth());
        BigDecimal prevExpenses = sum(userId, Transaction.TransactionType.EXPENSE, prevYm.atDay(1), prevYm.atEndOfMonth());

        BigDecimal prevBalance = prevIncome.subtract(prevExpenses);
        BigDecimal prevSavingsRate = BigDecimal.ZERO;
        if (prevIncome.compareTo(BigDecimal.ZERO) > 0) {
            prevSavingsRate = prevBalance.multiply(BigDecimal.valueOf(100)).divide(prevIncome, 2, RoundingMode.HALF_UP);
        }

        BigDecimal balance = curIncome.subtract(curExpenses);
        BigDecimal savingsRate = BigDecimal.ZERO;
        if (curIncome.compareTo(BigDecimal.ZERO) > 0) {
            savingsRate = balance.multiply(BigDecimal.valueOf(100)).divide(curIncome, 2, RoundingMode.HALF_UP);
        }

        BigDecimal incomeChange = calcPctChange(prevIncome, curIncome);
        BigDecimal expensesChange = calcPctChange(prevExpenses, curExpenses);
        BigDecimal balanceChange = calcPctChange(prevBalance, balance);
        BigDecimal savingsRateChange = savingsRate.subtract(prevSavingsRate);

        return DashboardDto.SummaryResponse.builder()
                .income(curIncome)
                .expenses(curExpenses)
                .balance(balance)
                .savingsRate(savingsRate)
                .balanceChange(balanceChange)
                .incomeChange(incomeChange)
                .expensesChange(expensesChange)
                .savingsRateChange(savingsRateChange)
                .build();
    }

    public List<TransactionDto.Response> getRecentTransactions(Long userId) {
        return transactionRepository.findTop10ByUserIdOrderByDateDescIdDesc(userId)
                .stream().map(this::mapToResponse).toList();
    }

    public List<DashboardDto.CategoryBreakdown> getCategoryBreakdown(Long userId) {
        YearMonth currentYm = YearMonth.now();
        List<Transaction> transactions = transactionRepository.findByUserIdAndDateBetweenOrderByDateDesc(
                userId, currentYm.atDay(1), currentYm.atEndOfMonth());

        Map<Transaction.Category, BigDecimal> map = transactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        BigDecimal total = map.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        List<DashboardDto.CategoryBreakdown> list = new ArrayList<>();
        map.forEach((cat, amt) -> {
            BigDecimal pct = total.compareTo(BigDecimal.ZERO) > 0 ?
                    amt.multiply(BigDecimal.valueOf(100)).divide(total, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            list.add(DashboardDto.CategoryBreakdown.builder()
                    .category(cat)
                    .amount(amt)
                    .percentage(pct)
                    .build());
        });

        list.sort(Comparator.comparing(DashboardDto.CategoryBreakdown::getAmount).reversed());
        return list;
    }

    public List<DashboardDto.MonthlyTrend> getMonthlyTrends(Long userId) {
        YearMonth currentYm = YearMonth.now();
        List<DashboardDto.MonthlyTrend> trends = new ArrayList<>();

        for (int i = 5; i >= 0; i--) {
            YearMonth ym = currentYm.minusMonths(i);
            BigDecimal inc = sum(userId, Transaction.TransactionType.INCOME, ym.atDay(1), ym.atEndOfMonth());
            BigDecimal exp = sum(userId, Transaction.TransactionType.EXPENSE, ym.atDay(1), ym.atEndOfMonth());
            trends.add(DashboardDto.MonthlyTrend.builder()
                    .month(ym.format(DateTimeFormatter.ofPattern("MMM yyyy")))
                    .income(inc)
                    .expenses(exp)
                    .build());
        }

        return trends;
    }

    public DashboardDto.AnalyticsSummary getAnalyticsSummary(Long userId) {
        DashboardDto.SummaryResponse summary = getSummary(userId);
        List<DashboardDto.CategoryBreakdown> categoryBreakdown = getCategoryBreakdown(userId);
        List<DashboardDto.MonthlyTrend> monthlyTrends = getMonthlyTrends(userId);

        BigDecimal avgMonthlySpend = monthlyTrends.stream()
                .map(DashboardDto.MonthlyTrend::getExpenses)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(Math.max(1, monthlyTrends.size())), 2, RoundingMode.HALF_UP);

        BigDecimal avgDailySpend = summary.getExpenses()
                .divide(BigDecimal.valueOf(Math.max(1, LocalDate.now().getDayOfMonth())), 2, RoundingMode.HALF_UP);

        String highestCategory = categoryBreakdown.isEmpty() ? "NONE" : categoryBreakdown.get(0).getCategory().name();

        List<Transaction> topTrans = transactionRepository.findByUserIdAndDateBetweenOrderByDateDesc(
                userId, YearMonth.now().atDay(1), YearMonth.now().atEndOfMonth());
        TransactionDto.Response largest = topTrans.stream()
                .max(Comparator.comparing(Transaction::getAmount))
                .map(this::mapToResponse)
                .orElse(null);

        return DashboardDto.AnalyticsSummary.builder()
                .summary(summary)
                .categoryBreakdown(categoryBreakdown)
                .monthlyTrends(monthlyTrends)
                .averageMonthlySpending(avgMonthlySpend)
                .averageDailySpending(avgDailySpend)
                .highestSpendingCategory(highestCategory)
                .largestTransaction(largest)
                .build();
    }

    private BigDecimal sum(Long userId, Transaction.TransactionType type, LocalDate start, LocalDate end) {
        BigDecimal val = transactionRepository.sumAmountByUserIdAndTypeAndDateBetween(userId, type, start, end);
        return val != null ? val : BigDecimal.ZERO;
    }

    private BigDecimal calcPctChange(BigDecimal prev, BigDecimal cur) {
        if (prev.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return cur.subtract(prev).multiply(BigDecimal.valueOf(100)).divide(prev, 2, RoundingMode.HALF_UP);
    }

    private TransactionDto.Response mapToResponse(Transaction t) {
        return TransactionDto.Response.builder()
                .id(t.getId())
                .type(t.getType())
                .amount(t.getAmount())
                .category(t.getCategory())
                .description(t.getDescription())
                .date(t.getDate())
                .paymentMethod(t.getPaymentMethod())
                .build();
    }
}
