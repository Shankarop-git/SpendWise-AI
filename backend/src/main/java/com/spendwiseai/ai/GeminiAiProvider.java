package com.spendwiseai.ai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spendwiseai.dto.AiDto;
import com.spendwiseai.entity.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
public class GeminiAiProvider implements AiProvider {

    private final String apiKey;
    private final String model;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String DISCLAIMER = "AI-generated insights are for informational and educational purposes only and should not be considered professional financial advice.";

    public GeminiAiProvider(
            @Value("${app.gemini.api-key}") String apiKey,
            @Value("${app.gemini.model:gemini-1.5-flash}") String model) {
        this.apiKey = apiKey;
        this.model = model;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public AiDto.InsightsResponse generateInsights(Map<String, Object> financialSummary) {
        String prompt = "You are SpendWise AI, a personal finance assistant. Analyze this compact financial summary and return ONLY valid JSON with keys: " +
                "\"summary\" (string), \"observations\" (list of strings), \"positiveTrends\" (list of strings), \"areasToImprove\" (list of strings), \"recommendations\" (list of strings). " +
                "Do NOT include markdown formatting or ```json code blocks in your final text. Financial Summary: " + toJson(financialSummary);

        String rawResponse = callGemini(prompt);
        return parseInsightsResponse(rawResponse, financialSummary);
    }

    @Override
    public AiDto.MonthlyReportResponse generateMonthlyReport(Map<String, Object> financialSummary) {
        String prompt = "You are SpendWise AI. Create a structured monthly financial report based ONLY on this compact data: " + toJson(financialSummary) + ". " +
                "Return ONLY valid JSON with keys: \"summary\" (string), \"keyObservations\" (array of strings), \"recommendations\" (array of strings). Do NOT wrap in markdown code blocks.";

        String rawResponse = callGemini(prompt);
        return parseMonthlyReportResponse(rawResponse, financialSummary);
    }

    @Override
    public AiDto.BudgetRecommendationResponse generateBudgetRecommendation(Map<String, Object> categorySummary) {
        String prompt = "You are SpendWise AI. Provide budget limit recommendations based on category spending context: " + toJson(categorySummary) + ". " +
                "Return ONLY valid JSON with keys: \"suggestedMinLimit\" (number), \"suggestedMaxLimit\" (number), \"reasoning\" (string). No markdown code blocks.";

        String rawResponse = callGemini(prompt);
        return parseBudgetRecommendationResponse(rawResponse, categorySummary);
    }

    @Override
    public String chat(String userMessage, Map<String, Object> financialContext) {
        String prompt = "You are SpendWise AI financial assistant for college students and young professionals. " +
                "Answer the user's question concisely based on their compact financial data: " + toJson(financialContext) + ". " +
                "User question: \"" + userMessage + "\". Provide a helpful response in clean plain text.";

        String rawResponse = callGemini(prompt);
        return cleanText(rawResponse);
    }

    private String callGemini(String promptText) {
        if ("demo-key".equals(apiKey) || apiKey.isBlank()) {
            log.warn("Gemini API key not configured or using default demo key. Using intelligent local fallback generator.");
            return "";
        }

        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey;
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> contents = Map.of(
                "contents", List.of(
                    Map.of("parts", List.of(Map.of("text", promptText)))
                )
            );

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(contents, headers);
            Map<String, Object> response = restTemplate.postForObject(url, requestEntity, Map.class);

            if (response != null && response.containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
                if (!candidates.isEmpty()) {
                    Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                    if (!parts.isEmpty()) {
                        return (String) parts.get(0).get("text");
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to communicate with Gemini API: {}", e.getMessage());
        }

        return "";
    }

    private AiDto.InsightsResponse parseInsightsResponse(String rawJson, Map<String, Object> summary) {
        try {
            String cleaned = cleanJson(rawJson);
            if (!cleaned.isEmpty()) {
                Map<String, Object> map = objectMapper.readValue(cleaned, new TypeReference<>() {});
                return AiDto.InsightsResponse.builder()
                        .summary((String) map.getOrDefault("summary", "Your monthly financial metrics look active."))
                        .observations(toStringList(map.get("observations")))
                        .positiveTrends(toStringList(map.get("positiveTrends")))
                        .areasToImprove(toStringList(map.get("areasToImprove")))
                        .recommendations(toStringList(map.get("recommendations")))
                        .disclaimer(DISCLAIMER)
                        .build();
            }
        } catch (Exception e) {
            log.warn("Parsing Gemini insights JSON failed. Generating fallback structured insight. Error: {}", e.getMessage());
        }

        // Fallback response using actual backend data summary
        BigDecimal totalExpenses = (BigDecimal) summary.getOrDefault("totalExpenses", BigDecimal.ZERO);
        BigDecimal totalIncome = (BigDecimal) summary.getOrDefault("totalIncome", BigDecimal.ZERO);
        BigDecimal savingsRate = (BigDecimal) summary.getOrDefault("savingsRate", BigDecimal.ZERO);

        return AiDto.InsightsResponse.builder()
                .summary("Based on your recorded income of $" + totalIncome + " and expenses of $" + totalExpenses + ", your current savings rate is " + savingsRate + "%.")
                .observations(List.of(
                        "Total income for the period: $" + totalIncome,
                        "Total expenses for the period: $" + totalExpenses
                ))
                .positiveTrends(List.of(
                        savingsRate.compareTo(BigDecimal.valueOf(20)) >= 0 ? "Great job maintaining a healthy savings rate above 20%!" : "Tracking transactions consistently helps identify savings opportunities."
                ))
                .areasToImprove(List.of(
                        "Monitor discretionary spending categories to optimize monthly savings."
                ))
                .recommendations(List.of(
                        "Aim to allocate at least 20% of net income towards savings and emergency funds.",
                        "Set up category budgets to avoid unexpected expense spikes."
                ))
                .disclaimer(DISCLAIMER)
                .build();
    }

    private AiDto.MonthlyReportResponse parseMonthlyReportResponse(String rawJson, Map<String, Object> summary) {
        BigDecimal totalIncome = (BigDecimal) summary.getOrDefault("totalIncome", BigDecimal.ZERO);
        BigDecimal totalExpenses = (BigDecimal) summary.getOrDefault("totalExpenses", BigDecimal.ZERO);
        BigDecimal savings = totalIncome.subtract(totalExpenses);
        BigDecimal savingsRate = (BigDecimal) summary.getOrDefault("savingsRate", BigDecimal.ZERO);

        try {
            String cleaned = cleanJson(rawJson);
            if (!cleaned.isEmpty()) {
                Map<String, Object> map = objectMapper.readValue(cleaned, new TypeReference<>() {});
                return AiDto.MonthlyReportResponse.builder()
                        .month("Current Month")
                        .totalIncome(totalIncome)
                        .totalExpenses(totalExpenses)
                        .savings(savings)
                        .savingsRate(savingsRate)
                        .topSpendingCategory((String) summary.getOrDefault("topCategory", "GENERAL"))
                        .summary((String) map.getOrDefault("summary", "Monthly spending report generated."))
                        .keyObservations(toStringList(map.get("keyObservations")))
                        .recommendations(toStringList(map.get("recommendations")))
                        .disclaimer(DISCLAIMER)
                        .build();
            }
        } catch (Exception e) {
            log.warn("Failed to parse Gemini monthly report response. Using structured fallback. Error: {}", e.getMessage());
        }

        return AiDto.MonthlyReportResponse.builder()
                .month("Current Month")
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .savings(savings)
                .savingsRate(savingsRate)
                .topSpendingCategory((String) summary.getOrDefault("topCategory", "GENERAL"))
                .summary("Monthly summary overview based on your recent activity.")
                .keyObservations(List.of(
                        "Net cash flow is " + (savings.compareTo(BigDecimal.ZERO) >= 0 ? "positive" : "negative") + " at $" + savings + ".",
                        "Savings rate currently stands at " + savingsRate + "%."
                ))
                .recommendations(List.of(
                        "Review recurring bill subscriptions.",
                        "Set monthly cap limits on high-frequency expense categories."
                ))
                .disclaimer(DISCLAIMER)
                .build();
    }

    private AiDto.BudgetRecommendationResponse parseBudgetRecommendationResponse(String rawJson, Map<String, Object> summary) {
        Transaction.Category category = (Transaction.Category) summary.get("category");
        BigDecimal avgSpend = (BigDecimal) summary.getOrDefault("averageSpend", BigDecimal.valueOf(300));
        BigDecimal minRec = avgSpend.multiply(BigDecimal.valueOf(0.9)).setScale(2, java.math.RoundingMode.HALF_UP);
        BigDecimal maxRec = avgSpend.multiply(BigDecimal.valueOf(1.1)).setScale(2, java.math.RoundingMode.HALF_UP);

        try {
            String cleaned = cleanJson(rawJson);
            if (!cleaned.isEmpty()) {
                Map<String, Object> map = objectMapper.readValue(cleaned, new TypeReference<>() {});
                return AiDto.BudgetRecommendationResponse.builder()
                        .category(category)
                        .suggestedMinLimit(map.get("suggestedMinLimit") != null ? new BigDecimal(map.get("suggestedMinLimit").toString()) : minRec)
                        .suggestedMaxLimit(map.get("suggestedMaxLimit") != null ? new BigDecimal(map.get("suggestedMaxLimit").toString()) : maxRec)
                        .recentAverageSpend(avgSpend)
                        .reasoning((String) map.getOrDefault("reasoning", "Recommendation calculated based on your historical average spending."))
                        .disclaimer(DISCLAIMER)
                        .build();
            }
        } catch (Exception e) {
            log.warn("Failed to parse Gemini budget recommendation JSON. Using fallback logic. Error: {}", e.getMessage());
        }

        return AiDto.BudgetRecommendationResponse.builder()
                .category(category)
                .suggestedMinLimit(minRec)
                .suggestedMaxLimit(maxRec)
                .recentAverageSpend(avgSpend)
                .reasoning("Based on your historical spending average of $" + avgSpend + " in " + category + ", we recommend setting a monthly cap between $" + minRec + " and $" + maxRec + ".")
                .disclaimer(DISCLAIMER)
                .build();
    }

    private String cleanJson(String raw) {
        if (raw == null) return "";
        String s = raw.trim();
        if (s.startsWith("```json")) {
            s = s.substring(7);
        } else if (s.startsWith("```")) {
            s = s.substring(3);
        }
        if (s.endsWith("```")) {
            s = s.substring(0, s.length() - 3);
        }
        return s.trim();
    }

    private String cleanText(String raw) {
        if (raw == null || raw.isBlank()) {
            return "SpendWise AI Assistant is ready. Ask me anything about your current income, expense breakdown, or budget status!";
        }
        return raw.trim();
    }

    private List<String> toStringList(Object obj) {
        if (obj instanceof List<?> list) {
            List<String> result = new ArrayList<>();
            for (Object item : list) {
                if (item != null) result.add(item.toString());
            }
            return result;
        }
        return List.of();
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "{}";
        }
    }
}
