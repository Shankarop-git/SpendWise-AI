package com.spendwiseai.ai;

import com.spendwiseai.dto.AiDto;
import java.util.Map;

public interface AiProvider {

    AiDto.InsightsResponse generateInsights(Map<String, Object> financialSummary);

    AiDto.MonthlyReportResponse generateMonthlyReport(Map<String, Object> financialSummary);

    AiDto.BudgetRecommendationResponse generateBudgetRecommendation(Map<String, Object> categorySummary);

    String chat(String userMessage, Map<String, Object> financialContext);
}
