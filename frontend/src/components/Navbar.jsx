import { Link } from "react-router-dom";
import { useState } from "react";
import { useAuth } from "../context/AuthContext";
import ProfileDropdown from "./ProfileDropdown";
import NotificationDropdown from "./NotificationDropdown";
import "../css/navbar.css";
import logo from "../assets/images/logo.png";
import profileImage from "../assets/images/profile2.png";

function Navbar() {
  const { user } = useAuth();
  const [showDropdown, setShowDropdown] = useState(false);

  return (
    <nav className="navbar">
      {/* LEFT */}
      <div className="nav-left">
        <img src={logo} alt="Aynak Logo" className="navbar-logo" />
      </div>
      {/* CENTER */}
      <div className="nav-center">
        {(user?.role === "CITIZEN" || user?.role === "ADMIN") && (
          <>
            <Link to="/dashboard">Home</Link>
            <Link to="/report">Report Incident</Link>
            <Link to="/report-ai">Report with AI</Link>
            <Link to="/my-reports">My Reports</Link>
            <Link to="/rewards">Rewards</Link>
            <Link to="/leaderboard">Leaderboard</Link>
            <Link to="/profile">Profile</Link>
          </>
        )}
        {user?.role === "AUTHORITY" && (
          <>
            <Link to="/authority-dashboard">Home</Link>
            <Link to="/approved-reports">Approved Reports</Link>
            <Link to="/authority-analytics">Analytics</Link>
            <Link to="/authority-profile">Profile</Link>
          </>
        )}
        {user?.role === "SPONSOR" && (
          <>
            <Link to="/sponsor-dashboard">Dashboard</Link>
            <Link to="/create-voucher">Create Voucher</Link>
            <Link to="/profile">Profile</Link>
          </>
        )}
      </div>
      {/* RIGHT */}
      <div className="nav-right">
        {(user?.role === "CITIZEN" || user?.role === "ADMIN") && (
          <NotificationDropdown />
        )}
        <div
          className="profile-circle"
          onClick={() => setShowDropdown(!showDropdown)}
        >
          <img src={profileImage} alt="Profile" />
        </div>
        {showDropdown && <ProfileDropdown />}
      </div>
    </nav>
  );
}
export default Navbar;
