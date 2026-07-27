import { apiClient } from "./client";

export interface Transaction {
  id: number;
  type: "INCOME" | "EXPENSE";
  amount: number;
  category: string;
  description: string;
  date: string;
  paymentMethod: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export const transactionApi = {
  create: (data: Partial<Transaction>) => apiClient.post<Transaction>("/transactions", data),
  update: (id: number, data: Partial<Transaction>) => apiClient.put<Transaction>(`/transactions/${id}`, data),
  delete: (id: number) => apiClient.delete<void>(`/transactions/${id}`),
  get: (id: number) => apiClient.get<Transaction>(`/transactions/${id}`),
  list: (params?: any) => apiClient.get<Page<Transaction>>("/transactions", params),
  exportCsv: () => apiClient.get<string>("/transactions/export"),
};
