package com.spendwiseai.dto;

import com.spendwiseai.entity.Transaction;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

public class BudgetDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull(message = "Category is required")
        private Transaction.Category category;

        @NotNull(message = "Monthly limit is required")
        @DecimalMin(value = "0.01", message = "Monthly limit must be positive")
        private BigDecimal monthlyLimit;

        @NotNull(message = "Month is required")
        @Min(value = 1, message = "Month must be between 1 and 12")
        @Max(value = 12, message = "Month must be between 1 and 12")
        private Integer month;

        @NotNull(message = "Year is required")
        private Integer year;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private Long id;
        private Transaction.Category category;
        private BigDecimal monthlyLimit;
        private BigDecimal spent;
        private BigDecimal remaining;
        private BigDecimal percentageUsed;
        private Status status;
        private Integer month;
        private Integer year;
    }

    public enum Status {
        NORMAL, WARNING, ALERT, EXCEEDED
    }
}
