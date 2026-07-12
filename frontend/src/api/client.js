import keycloak from "../auth/keycloak";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;

export async function apiFetch(path, options = {}) {
  if (keycloak.authenticated) {
    try {
      await keycloak.updateToken(30);
    } catch {
      keycloak.login();
    }
  }

  const headers = {
    ...options.headers,
    ...(keycloak.token ? { Authorization: `Bearer ${keycloak.token}` } : {}),
  };

  return fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers,
  });
}
