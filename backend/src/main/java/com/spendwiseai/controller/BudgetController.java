package com.spendwiseai.controller;

import com.spendwiseai.dto.BudgetDto;
import com.spendwiseai.security.UserPrincipal;
import com.spendwiseai.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
@Tag(name = "Budgets", description = "Budget management and progress tracking")
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    @Operation(summary = "Create or update a budget")
    public ResponseEntity<BudgetDto.Response> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody BudgetDto.Request request) {
        return ResponseEntity.ok(budgetService.createOrUpdateBudget(principal.getId(), request));
    }

    @GetMapping
    @Operation(summary = "List budgets for a month/year")
    public ResponseEntity<List<BudgetDto.Response>> list(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(budgetService.getBudgets(principal.getId(), month, year));
    }

    @GetMapping("/{id}/progress")
    @Operation(summary = "Get budget progress")
    public ResponseEntity<BudgetDto.Response> progress(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        return ResponseEntity.ok(budgetService.getBudgetProgress(principal.getId(), id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a budget")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long id) {
        budgetService.deleteBudget(principal.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
