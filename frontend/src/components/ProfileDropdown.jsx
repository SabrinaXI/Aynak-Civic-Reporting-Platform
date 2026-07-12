import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import "../css/profileDropdown.css";

function ProfileDropdown() {
  const { logout } = useAuth();

  return (
    <div className="profile-dropdown">
      <Link to="/profile">Profile</Link>
      <Link to="/settings">Settings</Link>
      <button onClick={logout}>Logout</button>
    </div>
  );
}

export default ProfileDropdown;
