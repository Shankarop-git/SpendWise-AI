import { toast } from "sonner";

export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8888/api";

class ApiClient {
  private getToken() {
    return localStorage.getItem("spendwise_token");
  }

  async request<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
    const url = `${API_BASE_URL}${endpoint}`;
    const token = this.getToken();
    
    const headers = new Headers(options.headers);
    if (token) {
      headers.set("Authorization", `Bearer ${token}`);
    }
    if (!(options.body instanceof FormData)) {
      headers.set("Content-Type", "application/json");
    }

    try {
      const response = await fetch(url, { ...options, headers });
      
      if (response.status === 401) {
        localStorage.removeItem("spendwise_token");
        // Don't hard-redirect; let the route guard handle navigation
        throw new Error("Unauthorized");
      }
      
      if (response.status === 429) {
        toast.error("Too many AI requests. Please wait a moment.");
      }

      if (!response.ok) {
        const errorData = await response.json().catch(() => null);
        throw new Error(errorData?.message || `HTTP error ${response.status}`);
      }

      // Check if response is JSON or empty
      const contentType = response.headers.get("content-type");
      if (contentType && contentType.includes("application/json")) {
        return response.json();
      }
      
      // For CSV export and endpoints returning text/no content
      if (contentType && contentType.includes("text/csv")) {
          return response.text() as any;
      }
      return null as T;

    } catch (error) {
      if (error instanceof Error) {
        toast.error(error.message);
      }
      throw error;
    }
  }

  get<T>(endpoint: string, params?: Record<string, string | number | boolean | undefined>) {
    let url = endpoint;
    if (params) {
      const searchParams = new URLSearchParams();
      Object.entries(params).forEach(([key, value]) => {
        if (value !== undefined && value !== null) {
          searchParams.append(key, String(value));
        }
      });
      const q = searchParams.toString();
      if (q) url += `?${q}`;
    }
    return this.request<T>(url, { method: "GET" });
  }

  post<T>(endpoint: string, data?: any) {
    return this.request<T>(endpoint, {
      method: "POST",
      body: data ? JSON.stringify(data) : undefined,
    });
  }

  put<T>(endpoint: string, data: any) {
    return this.request<T>(endpoint, {
      method: "PUT",
      body: JSON.stringify(data),
    });
  }

  delete<T>(endpoint: string) {
    return this.request<T>(endpoint, { method: "DELETE" });
  }
}

export const apiClient = new ApiClient();
