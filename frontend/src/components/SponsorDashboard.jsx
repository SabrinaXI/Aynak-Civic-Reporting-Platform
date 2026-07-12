import { useEffect, useState } from "react";
import Navbar from "./Navbar";
import "../css/sponsorDashboard.css";
import { apiFetch } from "../api/client";

function SponsorDashboard() {
  const [user, setUser] = useState(null);
  const [rewards, setRewards] = useState([]);

  useEffect(() => {
    loadUser();
    loadSponsorRewards();
  }, []);

  function loadUser() {
    apiFetch("/api/users/me")
      .then((response) => response.json())
      .then((data) => setUser(data))
      .catch((error) => console.log(error));
  }

  function loadSponsorRewards() {
    apiFetch("/api/rewards/sponsor/my")
      .then((response) => response.json())
      .then((data) => setRewards(data))
      .catch((error) => console.log(error));
  }

  const activeRewards = rewards.filter((reward) => reward.active).length;

  return (
    <>
      <Navbar />

      <main className="sponsor-page">
        <section className="sponsor-hero">
          <h1>Welcome, {user?.firstName || "Sponsor"}</h1>
          <p>
            Manage your sponsored rewards and help encourage citizens to improve
            the UAE community.
          </p>
        </section>

        <section className="sponsor-summary">
          <div className="sponsor-card">
            <h2>{rewards.length}</h2>
            <p>Total Vouchers</p>
          </div>

          <div className="sponsor-card">
            <h2>{activeRewards}</h2>
            <p>Active Vouchers</p>
          </div>

          <div className="sponsor-card">
            <h2>{user?.sponsor?.sponsorName || "-"}</h2>
            <p>Sponsor Name</p>
          </div>
        </section>

        <section className="sponsor-table-section">
          <h2>My Voucher Codes</h2>

          <div className="sponsor-table-wrapper">
            <table className="sponsor-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Brand</th>
                  <th>Title</th>
                  <th>Description</th>
                  <th>Code</th>
                  <th>Points</th>
                  <th>Status</th>
                </tr>
              </thead>

              <tbody>
                {rewards.length === 0 ? (
                  <tr>
                    <td colSpan="7" className="empty-row">
                      No vouchers created yet.
                    </td>
                  </tr>
                ) : (
                  rewards.map((reward) => (
                    <tr key={reward.id}>
                      <td>{reward.id}</td>
                      <td>{reward.brandName}</td>
                      <td>{reward.title}</td>
                      <td>{reward.description}</td>
                      <td>{reward.voucherCode}</td>
                      <td>{reward.pointsRequired}</td>
                      <td>{reward.active ? "Active" : "Inactive"}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </section>
      </main>
    </>
  );
}

export default SponsorDashboard;