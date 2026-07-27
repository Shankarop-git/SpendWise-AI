package com.spendwiseai.dto;

import com.spendwiseai.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

public class DashboardDto {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SummaryResponse {
        private BigDecimal income;
        private BigDecimal expenses;
        private BigDecimal balance;
        private BigDecimal savingsRate;
        private BigDecimal balanceChange;
        private BigDecimal incomeChange;
        private BigDecimal expensesChange;
        private BigDecimal savingsRateChange;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryBreakdown {
        private Transaction.Category category;
        private BigDecimal amount;
        private BigDecimal percentage;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MonthlyTrend {
        private String month;
        private BigDecimal income;
        private BigDecimal expenses;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AnalyticsSummary {
        private SummaryResponse summary;
        private List<CategoryBreakdown> categoryBreakdown;
        private List<MonthlyTrend> monthlyTrends;
        private BigDecimal averageMonthlySpending;
        private BigDecimal averageDailySpending;
        private String highestSpendingCategory;
        private TransactionDto.Response largestTransaction;
    }
}
