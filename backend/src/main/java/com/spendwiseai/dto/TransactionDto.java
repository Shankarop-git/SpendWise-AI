package com.spendwiseai.dto;

import com.spendwiseai.entity.Transaction;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull(message = "Transaction type is required")
        private Transaction.TransactionType type;

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be positive")
        private BigDecimal amount;

        @NotNull(message = "Category is required")
        private Transaction.Category category;

        private String description;

        @NotNull(message = "Date is required")
        private LocalDate date;

        @NotNull(message = "Payment method is required")
        private Transaction.PaymentMethod paymentMethod;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private Long id;
        private Transaction.TransactionType type;
        private BigDecimal amount;
        private Transaction.Category category;
        private String description;
        private LocalDate date;
        private Transaction.PaymentMethod paymentMethod;
    }
}
