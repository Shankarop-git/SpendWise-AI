import { apiClient } from "./client";
import { Transaction } from "./transactionApi";

export interface DashboardSummary {
  income: number;
  expenses: number;
  balance: number;
  savingsRate: number;
  balanceChange: number;
  incomeChange: number;
  expensesChange: number;
  savingsRateChange: number;
}

export interface CategoryBreakdown {
  category: string;
  amount: number;
  percentage: number;
}

export interface MonthlyTrend {
  month: string;
  income: number;
  expenses: number;
}

export interface AnalyticsSummary {
  summary: DashboardSummary;
  categoryBreakdown: CategoryBreakdown[];
  monthlyTrends: MonthlyTrend[];
  averageMonthlySpending: number;
  averageDailySpending: number;
  highestSpendingCategory: string;
  largestTransaction: Transaction | null;
}

export const dashboardApi = {
  getSummary: () => apiClient.get<DashboardSummary>("/dashboard/summary"),
  getRecentTransactions: () => apiClient.get<Transaction[]>("/dashboard/recent-transactions"),
  getCategoryBreakdown: () => apiClient.get<CategoryBreakdown[]>("/dashboard/expense-by-category"),
  getMonthlyTrends: () => apiClient.get<MonthlyTrend[]>("/dashboard/monthly-trends"),
};

export const analyticsApi = {
  getCategoryBreakdown: () => apiClient.get<CategoryBreakdown[]>("/analytics/category-breakdown"),
  getMonthlyTrends: () => apiClient.get<MonthlyTrend[]>("/analytics/monthly-trends"),
  getSummary: () => apiClient.get<AnalyticsSummary>("/analytics/summary"),
};
