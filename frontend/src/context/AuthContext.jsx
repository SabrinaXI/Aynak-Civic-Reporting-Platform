import { createContext, useContext, useEffect, useState } from "react";
import keycloak, { initKeycloak } from "../auth/keycloak";
import { apiFetch } from "../api/client";

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);                 //after logging in, the user will contain the info like name, role, instead of null
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    initKeycloak()
      .then((authenticated) => {
        if (!authenticated) {
          setUser(null);
          setLoading(false);
          return;
        }

        return apiFetch("/api/users/me")
          .then((response) => {
            if (!response.ok) {
              throw new Error("Not logged in");
            }

            return response.json();
          })
          .then((data) => {
            setUser(data);
            setLoading(false);
          });
      })
      .catch(() => {
        setUser(null);
        setLoading(false);
      });
  }, []);

  //logout
  const logout = () => {
    setUser(null);
    keycloak.logout({ redirectUri: `${window.location.origin}/` });
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        setUser,
        loading,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
