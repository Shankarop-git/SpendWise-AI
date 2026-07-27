import { apiClient } from "./client";

export interface UserResponse {
  id: number;
  name: string;
  email: string;
  isDemo: boolean;
}

export interface AuthResponse {
  token: string;
  user: UserResponse;
}

export const authApi = {
  login: (data: any) => apiClient.post<AuthResponse>("/auth/login", data),
  register: (data: any) => apiClient.post<AuthResponse>("/auth/register", data),
  getDemoAccount: () => apiClient.post<AuthResponse>("/auth/demo"),
  getCurrentUser: () => apiClient.get<UserResponse>("/auth/me"),
};
