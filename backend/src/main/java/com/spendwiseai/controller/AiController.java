package com.spendwiseai.controller;

import com.spendwiseai.dto.AiDto;
import com.spendwiseai.security.UserPrincipal;
import com.spendwiseai.service.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI Features", description = "Gemini AI-powered financial insights and assistant")
public class AiController {

    private final AiService aiService;

    @PostMapping("/insights")
    @Operation(summary = "Generate AI spending insights")
    public ResponseEntity<AiDto.InsightsResponse> insights(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(aiService.generateInsights(principal.getId()));
    }

    @PostMapping("/monthly-report")
    @Operation(summary = "Generate AI monthly financial report")
    public ResponseEntity<AiDto.MonthlyReportResponse> monthlyReport(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(aiService.generateMonthlyReport(principal.getId()));
    }

    @PostMapping("/budget-recommendation")
    @Operation(summary = "Get AI budget recommendation for a category")
    public ResponseEntity<AiDto.BudgetRecommendationResponse> budgetRecommendation(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody AiDto.BudgetRecommendationRequest request) {
        return ResponseEntity.ok(aiService.generateBudgetRecommendation(principal.getId(), request.getCategory()));
    }

    @PostMapping("/chat")
    @Operation(summary = "Chat with AI financial assistant")
    public ResponseEntity<AiDto.ChatResponse> chat(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody AiDto.ChatRequest request) {
        return ResponseEntity.ok(aiService.chat(principal.getId(), request.getMessage()));
    }
}
