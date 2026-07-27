import { apiClient } from "./client";

export interface AIInsight {
  summary: string;
  observations: string[];
  positiveTrends: string[];
  areasToImprove: string[];
  recommendations: string[];
  disclaimer: string;
}

export interface MonthlyReport {
  month: string;
  totalIncome: number;
  totalExpenses: number;
  savings: number;
  savingsRate: number;
  topSpendingCategory: string;
  highestTransactionDescription: string;
  highestTransactionAmount: number;
  summary: string;
  keyObservations: string[];
  recommendations: string[];
  disclaimer: string;
}

export interface BudgetRecommendation {
  category: string;
  suggestedMinLimit: number;
  suggestedMaxLimit: number;
  recentAverageSpend: number;
  reasoning: string;
  disclaimer: string;
}

export interface ChatResponse {
  reply: string;
  disclaimer: string;
}

export const aiApi = {
  getInsights: () => apiClient.post<AIInsight>("/ai/insights"),
  getMonthlyReport: () => apiClient.post<MonthlyReport>("/ai/monthly-report"),
  getBudgetRecommendation: (category: string) => apiClient.post<BudgetRecommendation>("/ai/budget-recommendation", { category }),
  chat: (message: string) => apiClient.post<ChatResponse>("/ai/chat", { message }),
};
