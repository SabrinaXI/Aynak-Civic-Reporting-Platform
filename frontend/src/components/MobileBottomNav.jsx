import { Link } from "react-router-dom";
import "../css/mobileBottomNav.css";

import homeIcon from "../assets/images/homeIconFilled1.png";
import reportsIcon from "../assets/images/reportIconFilled1.png";
import aiIcon from "../assets/images/aiIconFilled1.png";
import myReportsIcon from "../assets/images/myReportIconFilled1.png";
import rewardsIcon from "../assets/images/rewardIconFilled1.png";
import profileIcon from "../assets/images/profileIconFilled1.png";

function MobileBottomNav() {
  return (
    <div className="mobile-bottom-nav">
      <Link to="/dashboard">
        <img src={homeIcon} alt="Home" />
        <span>Home</span>
      </Link>
      <Link to="/report">
        <img src={reportsIcon} alt="Report Incident" />
        <span>Report</span>
      </Link>
      <Link to="/report-ai">
        <img src={aiIcon} alt="Report with AI" />
        <span>AI Report</span>
      </Link>
      <Link to="/my-reports">
        <img src={myReportsIcon} alt="My Reports" />
        <span>My Reports</span>
      </Link>
      <Link to="/rewards">
        <img src={rewardsIcon} alt="Rewards" />
        <span>Rewards</span>
      </Link>
      <Link to="/profile">
        <img src={profileIcon} alt="Profile" />
        <span>Profile</span>
      </Link>
    </div>
  );
}

export default MobileBottomNav;
