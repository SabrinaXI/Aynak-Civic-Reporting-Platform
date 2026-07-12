import { useEffect, useState } from "react";
import Navbar from "./Navbar";
import MobileBottomNav from "./MobileBottomNav";
import "../css/leaderboard.css";
import { apiFetch } from "../api/client";

import leaderboardBannerBG from "../assets/images/leaderboardBannerBG.png";
import profileIcon from "../assets/images/profile2.png";

function Leaderboard() {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    apiFetch("/api/users/leaderboard")
      .then((response) => response.json())
      .then((data) => setUsers(data.slice(0, 10)))
      .catch((error) => console.log(error));
  }, []);

  const topThree = users.slice(0, 3);
  const topTen = users.slice(0, 10);

  return (
    <>
      <Navbar />

      <div className="leaderboard-page">
        <section
          className="leaderboard-banner"
          style={{ backgroundImage: `url(${leaderboardBannerBG})` }}
        >
          <h1>All Time Top Users</h1>

          <div className="top-users">
            {topThree[1] && (
              <div className="top-user second-place">
                <span className="top-rank-label">2nd</span>
                <div className="top-circle">
                  <img src={profileIcon} alt="Profile" />
                </div>
                <h2>{topThree[1].fullName}</h2>
                <p>{topThree[1].approvedReports} reports approved</p>
              </div>
            )}

            {topThree[0] && (
              <div className="top-user first-place">
                <span className="top-rank-label first-label">1st</span>
                <div className="top-circle first">
                  <img src={profileIcon} alt="Profile" />
                </div>
                <h2>{topThree[0].fullName}</h2>
                <p>{topThree[0].approvedReports} reports approved</p>
              </div>
            )}

            {topThree[2] && (
              <div className="top-user third-place">
                <span className="top-rank-label">3rd</span>
                <div className="top-circle">
                  <img src={profileIcon} alt="Profile" />
                </div>
                <h2>{topThree[2].fullName}</h2>
                <p>{topThree[2].approvedReports} reports approved</p>
              </div>
            )}
          </div>
        </section>

        <section className="leaderboard-list">
          {topTen.map((user) => (
            <div className="leaderboard-row" key={user.userId}>
              <div className="leaderboard-user">
                <span className="rank-number">{user.rank}</span>

                <div className="small-profile-circle">
                  <img src={profileIcon} alt="Profile" />
                </div>

                <span>{user.fullName}</span>
              </div>

              <p>{user.approvedReports}</p>
            </div>
          ))}
        </section>
      </div>

      <MobileBottomNav />
    </>
  );
}

export default Leaderboard;
