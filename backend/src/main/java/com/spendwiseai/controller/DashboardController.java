package com.spendwiseai.controller;

import com.spendwiseai.dto.DashboardDto;
import com.spendwiseai.dto.TransactionDto;
import com.spendwiseai.security.UserPrincipal;
import com.spendwiseai.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "Financial summary and trends")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    @Operation(summary = "Get financial summary")
    public ResponseEntity<DashboardDto.SummaryResponse> summary(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(dashboardService.getSummary(principal.getId()));
    }

    @GetMapping("/recent-transactions")
    @Operation(summary = "Get 10 most recent transactions")
    public ResponseEntity<List<TransactionDto.Response>> recentTransactions(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(dashboardService.getRecentTransactions(principal.getId()));
    }

    @GetMapping("/expense-by-category")
    @Operation(summary = "Get expense breakdown by category")
    public ResponseEntity<List<DashboardDto.CategoryBreakdown>> expenseByCategory(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(dashboardService.getCategoryBreakdown(principal.getId()));
    }

    @GetMapping("/monthly-trends")
    @Operation(summary = "Get 6-month income/expense trends")
    public ResponseEntity<List<DashboardDto.MonthlyTrend>> monthlyTrends(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(dashboardService.getMonthlyTrends(principal.getId()));
    }
}
