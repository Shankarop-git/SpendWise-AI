import { useRouteContext } from "@tanstack/react-router";
import type { UserResponse } from "@/api/authApi";

/**
 * Returns the authenticated user loaded by the /app route's beforeLoad guard.
 * Must be called from a component rendered under the /app route tree.
 */
export function useCurrentUser(): UserResponse {
  const { user } = useRouteContext({ from: "/app" });
  return user as UserResponse;
}

/**
 * Derive display initials from a full name string.
 * "Jane Doe" → "JD", "Alice" → "AL"
 */
export function getInitials(name: string): string {
  const parts = name.trim().split(/\s+/);
  if (parts.length >= 2) {
    return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
  }
  return name.slice(0, 2).toUpperCase();
}
