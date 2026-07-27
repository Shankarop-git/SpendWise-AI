package com.spendwiseai.dto;

import com.spendwiseai.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

public class AiDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatRequest {
        private String message;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ChatResponse {
        private String reply;
        private String disclaimer;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class InsightsResponse {
        private String summary;
        private List<String> observations;
        private List<String> positiveTrends;
        private List<String> areasToImprove;
        private List<String> recommendations;
        private String disclaimer;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MonthlyReportResponse {
        private String month;
        private BigDecimal totalIncome;
        private BigDecimal totalExpenses;
        private BigDecimal savings;
        private BigDecimal savingsRate;
        private String topSpendingCategory;
        private String highestTransactionDescription;
        private BigDecimal highestTransactionAmount;
        private String summary;
        private List<String> keyObservations;
        private List<String> recommendations;
        private String disclaimer;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BudgetRecommendationRequest {
        private Transaction.Category category;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BudgetRecommendationResponse {
        private Transaction.Category category;
        private BigDecimal suggestedMinLimit;
        private BigDecimal suggestedMaxLimit;
        private BigDecimal recentAverageSpend;
        private String reasoning;
        private String disclaimer;
    }
}
