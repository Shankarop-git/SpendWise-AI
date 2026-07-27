package com.spendwiseai.controller;

import com.spendwiseai.dto.TransactionDto;
import com.spendwiseai.entity.Transaction;
import com.spendwiseai.security.UserPrincipal;
import com.spendwiseai.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Income and expense CRUD operations")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Create a new transaction")
    public ResponseEntity<TransactionDto.Response> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody TransactionDto.Request request) {
        return ResponseEntity.ok(transactionService.createTransaction(principal.getId(), request));
    }

    @GetMapping
    @Operation(summary = "List transactions with filters and pagination")
    public ResponseEntity<Page<TransactionDto.Response>> list(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) Transaction.TransactionType type,
            @RequestParam(required = false) Transaction.Category category,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(transactionService.getTransactions(
                principal.getId(), type, category, startDate, endDate, search, page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID")
    public ResponseEntity<TransactionDto.Response> getById(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(principal.getId(), id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a transaction")
    public ResponseEntity<TransactionDto.Response> update(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id,
            @Valid @RequestBody TransactionDto.Request request) {
        return ResponseEntity.ok(transactionService.updateTransaction(principal.getId(), id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a transaction")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        transactionService.deleteTransaction(principal.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export")
    @Operation(summary = "Export all transactions as CSV")
    public ResponseEntity<byte[]> exportCsv(@AuthenticationPrincipal UserPrincipal principal) {
        String csv = transactionService.exportCsv(principal.getId());
        byte[] bytes = csv.getBytes();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(bytes);
    }
}
