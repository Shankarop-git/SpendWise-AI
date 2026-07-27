package com.spendwiseai.controller;

import com.spendwiseai.dto.DashboardDto;
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
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "Spending analysis and financial insights")
public class AnalyticsController {

    private final DashboardService dashboardService;

    @GetMapping("/category-breakdown")
    @Operation(summary = "Get expense category breakdown")
    public ResponseEntity<List<DashboardDto.CategoryBreakdown>> categoryBreakdown(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(dashboardService.getCategoryBreakdown(principal.getId()));
    }

    @GetMapping("/monthly-trends")
    @Operation(summary = "Get monthly income vs expenses trends")
    public ResponseEntity<List<DashboardDto.MonthlyTrend>> monthlyTrends(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(dashboardService.getMonthlyTrends(principal.getId()));
    }

    @GetMapping("/summary")
    @Operation(summary = "Get comprehensive analytics summary")
    public ResponseEntity<DashboardDto.AnalyticsSummary> analyticsSummary(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(dashboardService.getAnalyticsSummary(principal.getId()));
    }
}
