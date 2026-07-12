import { useEffect, useState } from "react";

import Navbar from "./Navbar";

import "../css/authorityDashboard.css";
import { apiFetch } from "../api/client";

function AuthorityDashboard() {
  const [reports, setReports] = useState([]);

  const [allReports, setAllReports] = useState([]);

  useEffect(() => {
    loadAllReports();

    loadPendingReports();
  }, []);

  function loadAllReports() {
    apiFetch("/api/reports")
      .then((response) => response.json())

      .then((data) => setAllReports(data))

      .catch((error) => console.log(error));
  }

  function loadPendingReports() {
    apiFetch("/api/reports/authority/pending")
      .then((response) => response.json())

      .then((data) => setReports(data))

      .catch((error) => console.log(error));
  }

  function viewPhoto(id) {
    apiFetch(`/api/reports/photo/${id}`)
      .then((response) => response.blob())
      .then((blob) => {
        window.open(URL.createObjectURL(blob), "_blank", "noreferrer");
      })
      .catch((error) => console.log(error));
  }

  function approveReport(id) {
    apiFetch(`/api/reports/authority/${id}/approve`, {
      method: "PUT",
    })
      .then((response) => {
        if (!response.ok) throw new Error("Failed to approve report");

        return response.text();
      })

      .then(() => {
        alert("Report approved successfully");

        loadAllReports();

        loadPendingReports();
      })

      .catch((error) => {
        console.log(error);

        alert("Something went wrong");
      });
  }

  function rejectReport(id) {
    apiFetch(`/api/reports/authority/${id}/reject`, {
      method: "PUT",
    })
      .then((response) => {
        if (!response.ok) throw new Error("Failed to reject report");

        return response.text();
      })

      .then(() => {
        alert("Report rejected successfully");

        loadAllReports();

        loadPendingReports();
      })

      .catch((error) => {
        console.log(error);

        alert("Something went wrong");
      });
  }

  const totalReports = allReports.length;

  const pendingReports = allReports.filter(
    (report) => report.status === "PENDING",
  ).length;

  const approvedReports = allReports.filter(
    (report) => report.status === "APPROVED",
  ).length;

  const inProgressReports = allReports.filter(
    (report) => report.status === "IN_PROGRESS",
  ).length;

  const resolvedReports = allReports.filter(
    (report) => report.status === "RESOLVED",
  ).length;

  const rejectedReports = allReports.filter(
    (report) => report.status === "REJECTED",
  ).length;

  return (
    <>
      <Navbar />
      <main className="authority-page">
        <section className="authority-hero">
          <h1>Pending Reports</h1>
          <p>
            The community has reported. Review, verify, and help turn reports
            into action.
          </p>
        </section>
        <section className="authority-summary">
          <div className="summary-card">
            <h2>{totalReports}</h2>
            <p>Total Reports</p>
          </div>
          <div className="summary-card">
            <h2>{pendingReports}</h2>
            <p>Pending Reports</p>
          </div>
          <div className="summary-card">
            <h2>{approvedReports}</h2>
            <p>Approved Reports</p>
          </div>
          <div className="summary-card">
            <h2>{inProgressReports}</h2>
            <p>In Progress</p>
          </div>
          <div className="summary-card">
            <h2>{resolvedReports}</h2>
            <p>Resolved Reports</p>
          </div>
          <div className="summary-card">
            <h2>{rejectedReports}</h2>
            <p>Rejected Reports</p>
          </div>
        </section>
        <section className="authority-table-section">
          <h2>Pending Reports</h2>
          <div className="authority-table-wrapper">
            <table className="authority-table">
              <thead>
                <tr>
                  <th>Report ID</th>
                  <th>Reporter Name</th>
                  <th>Email</th>
                  <th>Phone</th>
                  <th>Category</th>
                  <th>Title</th>
                  <th>Location</th>
                  <th>Submitted On</th>
                  <th>Image</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {reports.length === 0 ? (
                  <tr>
                    <td colSpan="10" className="empty-row">
                      No pending reports.
                    </td>
                  </tr>
                ) : (
                  reports.map((report) => (
                    <tr key={report.id}>
                      <td>{report.id}</td>
                      <td>
                        {report.citizen?.firstName} {report.citizen?.lastName}
                      </td>
                      <td>{report.citizen?.email}</td>
                      <td>{report.citizen?.phoneNumber || "-"}</td>
                      <td>{report.category}</td>
                      <td>{report.title}</td>
                      <td>{report.locationName}</td>
                      <td>
                        {report.createdAt
                          ? report.createdAt.substring(0, 10)
                          : "-"}
                      </td>
                      <td>
                        <a
                          href="#"
                          onClick={(e) => {
                            e.preventDefault();
                            viewPhoto(report.id);
                          }}
                          className="view-link"
                        >
                          View
                        </a>
                      </td>
                      <td>
                        <div className="action-buttons">
                          <button
                            className="approve-btn"
                            onClick={() => approveReport(report.id)}
                          >
                            Approve
                          </button>
                          <button
                            className="reject-btn"
                            onClick={() => rejectReport(report.id)}
                          >
                            Reject
                          </button>
                        </div>
                      </td>
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

export default AuthorityDashboard;
