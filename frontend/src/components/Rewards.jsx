import { useEffect, useState } from "react";

import Navbar from "./Navbar";

import MobileBottomNav from "./MobileBottomNav";

import "../css/rewards.css";

import rewardsBannerBG from "../assets/images/rewardsBannerBG.png";

import carrefourLogo from "../assets/images/carrefourLogo.png";

import luluLogo from "../assets/images/luluLogo.png";

import { apiFetch } from "../api/client";

function Rewards() {
  const [userData, setUserData] = useState(null);

  const [rewards, setRewards] = useState([]);

  const [redeemedRewards, setRedeemedRewards] = useState([]);

  const logoMap = {
    CARREFOUR: carrefourLogo,

    LULU: luluLogo,
  };

  useEffect(() => {
    loadUser();

    loadRewards();

    loadRedeemedRewards();
  }, []);

  function loadUser() {
    apiFetch("/api/users/me")
      .then((response) => response.json())

      .then((data) => setUserData(data))

      .catch((error) => console.log(error));
  }

  function loadRewards() {
    apiFetch("/api/rewards")
      .then((response) => response.json())

      .then((data) => setRewards(data))

      .catch((error) => console.log(error));
  }

  function loadRedeemedRewards() {
    apiFetch("/api/rewards/my-redeemed")
      .then((response) => response.json())

      .then((data) => setRedeemedRewards(data))

      .catch((error) => console.log(error));
  }

  function redeemReward(rewardId) {
    apiFetch(`/api/rewards/${rewardId}/redeem`, {
      method: "POST",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Could not redeem reward");
        }

        return response.json();
      })

      .then(() => {
        alert("Reward redeemed successfully!");

        loadUser();

        loadRewards();

        loadRedeemedRewards();
      })

      .catch((error) => {
        console.log(error);

        alert("Not enough points or something went wrong.");
      });
  }

  function copyCode(code) {
    navigator.clipboard.writeText(code);

    alert("Voucher code copied!");
  }

  const availableRewards = rewards.filter(
    (reward) => !redeemedRewards.some((item) => item.reward.id === reward.id),
  );

  return (
    <>
      <Navbar />
      <main className="rewards-page">
        <section
          className="rewards-banner"
          style={{ backgroundImage: `url(${rewardsBannerBG})` }}
        >
          <div className="banner-content">
            <h1>
              Thank you for looking after the <span>Community!</span>
            </h1>
            <div className="points-box">
              <p>Your Aynak Points</p>
              <h2>{userData?.aynakPoints || 0}</h2>
            </div>
          </div>
        </section>
        <section className="rewards-header">
          <h2>Redeem Rewards</h2>
          <p>Use your Aynak Points to redeem vouchers and rewards.</p>
        </section>
        <section className="voucher-list">
          {availableRewards.length === 0 ? (
            <p className="no-redeemed">No rewards available to redeem.</p>
          ) : (
            availableRewards.map((reward) => (
              <div className="voucher-card" key={reward.id}>
                <div className="voucher-brand">
                  <img
                    src={logoMap[reward.brandName?.toUpperCase()]}
                    alt={reward.brandName}
                  />
                  <h3>{reward.brandName}</h3>
                </div>
                <div className="voucher-divider"></div>
                <div className="voucher-details">
                  <h2>{reward.title}</h2>
                  <p>{reward.description}</p>
                  <div className="voucher-code hidden-code">
                    <span>Redeem to reveal code</span>
                  </div>
                </div>
                <div className="voucher-action">
                  <button type="button" onClick={() => redeemReward(reward.id)}>
                    Redeem Code
                  </button>
                  <p>{reward.pointsRequired} Points</p>
                </div>
              </div>
            ))
          )}
        </section>
        <section className="rewards-header redeemed-title">
          <h2>Rewards You Redeemed</h2>
          <p>Your redeemed voucher codes are saved here.</p>
        </section>
        <section className="voucher-list">
          {redeemedRewards.length === 0 ? (
            <p className="no-redeemed">No redeemed rewards yet.</p>
          ) : (
            redeemedRewards.map((item) => (
              <div className="voucher-card redeemed-card" key={item.id}>
                <div className="voucher-brand">
                  <img
                    src={logoMap[item.reward.brandName?.toUpperCase()]}
                    alt={item.reward.brandName}
                  />
                  <h3>{item.reward.brandName}</h3>
                </div>
                <div className="voucher-divider"></div>
                <div className="voucher-details">
                  <h2>{item.reward.title}</h2>
                  <p>
                    Redeemed on{" "}
                    {item.redeemedAt ? item.redeemedAt.substring(0, 10) : "-"}
                  </p>
                  <div className="voucher-code">
                    <span>{item.voucherCode}</span>
                    <button
                      type="button"
                      onClick={() => copyCode(item.voucherCode)}
                    >
                      Copy
                    </button>
                  </div>
                </div>
                <div className="voucher-action">
                  <p>Already Redeemed</p>
                </div>
              </div>
            ))
          )}
        </section>
      </main>
      <MobileBottomNav />
    </>
  );
}

export default Rewards;
