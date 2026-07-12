import { useEffect, useState } from "react";

import Navbar from "./Navbar";

import "../css/authorityProfile.css";
import { apiFetch } from "../api/client";

function AuthorityProfile() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    apiFetch("/api/users/me")
      .then((response) => response.json())

      .then((data) => setUser(data))

      .catch((error) => console.log(error));
  }, []);

  return (
    <>
      <Navbar />
      <main className="authority-profile-page">
        <section className="authority-profile-hero">
          <h1>Authority Profile</h1>
          <p>View your authority account and organization details.</p>
        </section>
        <section className="authority-profile-card">
          <div className="authority-profile-header">
            <div className="authority-avatar">
              {user?.firstName?.charAt(0)}

              {user?.lastName?.charAt(0)}
            </div>
            <div>
              <h2>
                {user?.firstName} {user?.lastName}
              </h2>
              <p>{user?.jobTitle || "Authority Officer"}</p>
            </div>
          </div>
          <div className="authority-profile-grid">
            <div className="profile-field">
              <span>Email</span>
              <p>{user?.email || "-"}</p>
            </div>
            <div className="profile-field">
              <span>Role</span>
              <p>{user?.role || "-"}</p>
            </div>
            <div className="profile-field">
              <span>Authority Name</span>
              <p>{user?.authority?.authorityName || "-"}</p>
            </div>
            <div className="profile-field">
              <span>Authority Category</span>
              <p>{user?.authority?.authorityCategory || "-"}</p>
            </div>
            <div className="profile-field">
              <span>Authority Email</span>
              <p>{user?.authority?.contactEmail || "-"}</p>
            </div>
            <div className="profile-field">
              <span>Member Since</span>
              <p>{user?.createdAt ? user.createdAt.substring(0, 10) : "-"}</p>
            </div>
          </div>
          <div className="authority-description">
            <span>Description</span>
            <p>{user?.authority?.description || "-"}</p>
          </div>
        </section>
      </main>
    </>
  );
}

export default AuthorityProfile;
