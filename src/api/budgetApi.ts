import { apiClient } from "./client";

export interface Budget {
  id: number;
  category: string;
  monthlyLimit: number;
  spent: number;
  remaining: number;
  percentageUsed: number;
  status: "NORMAL" | "WARNING" | "ALERT" | "EXCEEDED";
  month: number;
  year: number;
}

export const budgetApi = {
  createOrUpdate: (data: Partial<Budget>) => apiClient.post<Budget>("/budgets", data),
  list: (month?: number, year?: number) => apiClient.get<Budget[]>("/budgets", { month, year }),
  getProgress: (id: number) => apiClient.get<Budget>(`/budgets/${id}/progress`),
  delete: (id: number) => apiClient.delete<void>(`/budgets/${id}`),
};
