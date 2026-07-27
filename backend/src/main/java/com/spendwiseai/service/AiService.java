package com.spendwiseai.service;

import com.spendwiseai.ai.AiProvider;
import com.spendwiseai.dto.AiDto;
import com.spendwiseai.dto.DashboardDto;
import com.spendwiseai.entity.Transaction;
import com.spendwiseai.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiService {

    private final AiProvider aiProvider;
    private final DashboardService dashboardService;
    private final TransactionRepository transactionRepository;

    public AiDto.InsightsResponse generateInsights(Long userId) {
        Map<String, Object> summary = buildCompactSummary(userId);
        return aiProvider.generateInsights(summary);
    }

    public AiDto.MonthlyReportResponse generateMonthlyReport(Long userId) {
        Map<String, Object> summary = buildCompactSummary(userId);
        return aiProvider.generateMonthlyReport(summary);
    }

    public AiDto.BudgetRecommendationResponse generateBudgetRecommendation(Long userId, Transaction.Category category) {
        Map<String, Object> categorySummary = buildCategorySummary(userId, category);
        return aiProvider.generateBudgetRecommendation(categorySummary);
    }

    public AiDto.ChatResponse chat(Long userId, String message) {
        Map<String, Object> context = buildCompactSummary(userId);
        String reply = aiProvider.chat(message, context);
        return AiDto.ChatResponse.builder()
                .reply(reply)
                .disclaimer("AI-generated insights are for informational and educational purposes only and should not be considered professional financial advice.")
                .build();
    }

    /**
     * Build a compact financial summary for AI prompts — never sends raw transaction list.
     */
    private Map<String, Object> buildCompactSummary(Long userId) {
        DashboardDto.SummaryResponse summary = dashboardService.getSummary(userId);
        List<DashboardDto.CategoryBreakdown> categories = dashboardService.getCategoryBreakdown(userId);
        List<DashboardDto.MonthlyTrend> trends = dashboardService.getMonthlyTrends(userId);

        Map<String, Object> compact = new LinkedHashMap<>();
        compact.put("totalIncome", summary.getIncome());
        compact.put("totalExpenses", summary.getExpenses());
        compact.put("balance", summary.getBalance());
        compact.put("savingsRate", summary.getSavingsRate());
        compact.put("incomeChange", summary.getIncomeChange());
        compact.put("expenseChange", summary.getExpensesChange());

        // Top categories (max 6)
        List<Map<String, Object>> topCats = categories.stream()
                .limit(6)
                .map(c -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("category", c.getCategory().name());
                    m.put("amount", c.getAmount());
                    m.put("percentage", c.getPercentage());
                    return m;
                })
                .toList();
        compact.put("topCategories", topCats);

        if (!categories.isEmpty()) {
            compact.put("topCategory", categories.get(0).getCategory().name());
        }

        // Monthly trends (compact)
        List<Map<String, Object>> trendData = trends.stream()
                .map(t -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("month", t.getMonth());
                    m.put("income", t.getIncome());
                    m.put("expenses", t.getExpenses());
                    return m;
                })
                .toList();
        compact.put("monthlyTrends", trendData);

        return compact;
    }

    /**
     * Build category-specific spending summary for budget recommendations.
     */
    private Map<String, Object> buildCategorySummary(Long userId, Transaction.Category category) {
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("category", category);

        YearMonth current = YearMonth.now();
        List<BigDecimal> monthlySpends = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            YearMonth ym = current.minusMonths(i);
            BigDecimal spent = transactionRepository.sumExpenseByUserIdAndCategoryAndDateBetween(
                    userId, category, ym.atDay(1), ym.atEndOfMonth());
            if (spent == null) spent = BigDecimal.ZERO;
            monthlySpends.add(spent);
        }

        summary.put("last3MonthsSpend", monthlySpends);

        BigDecimal avg = monthlySpends.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
        summary.put("averageSpend", avg);

        return summary;
    }
}
