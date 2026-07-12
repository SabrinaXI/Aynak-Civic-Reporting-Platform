import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import keycloak from "../auth/keycloak";
import "../css/landing.css";

const ROLE_HOME = {
  CITIZEN: "/dashboard",
  ADMIN: "/dashboard",
  AUTHORITY: "/authority-dashboard",
  SPONSOR: "/sponsor-dashboard",
};

function Landing() {
  const { user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (user && ROLE_HOME[user.role]) {
      navigate(ROLE_HOME[user.role]);
    }
  }, [user, navigate]);

  return (
    <div className="landing-page">
      <div className="landing-content">
        <h1>Aynak</h1>
        <p>Together for a better UAE</p>
        <div className="landing-buttons">
          <button
            className="landing-btn"
            onClick={() => keycloak.login({ redirectUri: `${window.location.origin}/` })}
          >
            Sign in
          </button>
          <button
            className="landing-btn"
            onClick={() => keycloak.register({ redirectUri: `${window.location.origin}/` })}
          >
            Sign up
          </button>
        </div>
      </div>
    </div>
  );
}

export default Landing;
