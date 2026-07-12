import { useEffect, useState } from "react";
import Navbar from "./Navbar";
import MobileBottomNav from "./MobileBottomNav";
import "../css/profile.css";
import profileBannerBG from "../assets/images/profileBannerBG.png";
import profileIcon from "../assets/images/profileIconFilled2.png";
import { apiFetch } from "../api/client";

function Profile() {
  const [userData, setUserData] = useState(null);
  const [myReports, setMyReports] = useState([]);
  const [rankData, setRankData] = useState(null);

  useEffect(() => {
    apiFetch("/api/users/me")
      .then((response) => response.json())

      .then((data) => setUserData(data))

      .catch((error) => console.log(error));
  }, []);

  useEffect(() => {
    apiFetch("/api/reports/my")
      .then((response) => response.json())

      .then((data) => setMyReports(data))

      .catch((error) => console.log(error));
  }, []);

  useEffect(() => {
    apiFetch("/api/users/me/rank")
      .then((response) => response.json())
      .then((data) => setRankData(data))
      .catch((error) => console.log(error));
  }, []);

  const fullName = `${userData?.firstName || ""} ${userData?.lastName || ""}`;

  const resolvedReports = myReports.filter(
    (report) => report.status === "RESOLVED",
  ).length;

  return (
    <>
      <Navbar />
      <div className="profile-page">
        <div
          className="profile-banner"
          style={{ backgroundImage: `url(${profileBannerBG})` }}
        ></div>
        <div className="profile-main">
          <div className="profile-avatar">
            <img src={profileIcon} alt="Profile" />
          </div>
          <div className="profile-name-row">
            <h1>{fullName}</h1>
            <button className="edit-profile-btn">Edit Profile</button>
          </div>
          <div className="profile-content">
            <section className="profile-card">
              <h2>Profile Information</h2>
              <div className="profile-field">
                <span>First Name</span>
                <p>{userData?.firstName || "-"}</p>
              </div>
              <div className="profile-field">
                <span>Last Name</span>
                <p>{userData?.lastName || "-"}</p>
              </div>
              <div className="profile-field">
                <span>Email</span>
                <p>{userData?.email || "-"}</p>
              </div>
              <div className="profile-field">
                <span>DOB</span>
                <p>{userData?.dateOfBirth || "-"}</p>
              </div>
              <div className="profile-field">
                <span>Phone Number</span>
                <p>{userData?.phoneNumber || "-"}</p>
              </div>
              <div className="profile-field">
                <span>Emirate</span>
                <p>{userData?.emirate || "-"}</p>
              </div>
              <div className="profile-field">
                <span>Member Since</span>
                <p>
                  {userData?.createdAt
                    ? userData.createdAt.substring(0, 10)
                    : "-"}
                </p>
              </div>
              <div className="profile-field">
                <span>Preferred Language</span>
                <p>{userData?.preferredLanguage || "-"}</p>
              </div>
            </section>
            <section className="profile-card overview-card">
              <h2>Profile Overview</h2>
              <div className="overview-row">
                <span>Aynak Points</span>
                <p>{userData?.aynakPoints || 0}</p>
              </div>
              <div className="overview-row">
                <span>Reports Submitted</span>
                <p>{myReports.length}</p>
              </div>
              <div className="overview-row">
                <span>Reports Resolved</span>
                <p>{resolvedReports}</p>
              </div>
              <div className="overview-row">
                <span>Leaderboard Rank</span>
                <p>{rankData?.rank || "-"}</p>
              </div>
            </section>
          </div>
        </div>
      </div>
      <MobileBottomNav />
    </>
  );
}

export default Profile;
