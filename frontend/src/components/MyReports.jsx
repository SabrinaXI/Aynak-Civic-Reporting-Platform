import { useEffect, useState } from "react";
import Navbar from "./Navbar";
import MobileBottomNav from "./MobileBottomNav";
import "../css/myReports.css";
import { apiFetch } from "../api/client";

function MyReports() {
  const [reports, setReports] = useState([]);
  const [statusFilter, setStatusFilter] = useState("");
  const [photoUrls, setPhotoUrls] = useState({});

  useEffect(() => {
    loadMyReports();
  }, []);

  useEffect(() => {
    reports.forEach((report) => {
      if (photoUrls[report.id]) return;

      apiFetch(`/api/reports/photo/${report.id}`)
        .then((response) => response.blob())
        .then((blob) => {
          setPhotoUrls((prev) => ({
            ...prev,
            [report.id]: URL.createObjectURL(blob),
          }));
        })
        .catch((error) => console.log(error));
    });
  }, [reports]);

  function loadMyReports() {
    apiFetch("/api/reports/my")
      .then((response) => response.json())
      .then((data) => setReports(data))
      .catch((error) => console.log(error));
  }

  function deleteReport(id) {
    const confirmDelete = window.confirm(
      "Are you sure you want to delete this pending report?",
    );

    if (!confirmDelete) return;

    apiFetch(`/api/reports/my/${id}`, {
      method: "DELETE",
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to delete report");
        }

        return response.text();
      })
      .then(() => {
        setReports(reports.filter((report) => report.id !== id));
      })
      .catch((error) => {
        console.log(error);
        alert("Something went wrong");
      });
  }

  const filteredReports =
    statusFilter === ""
      ? reports
      : reports.filter((report) => report.status === statusFilter);

  return (
    <>
      <Navbar />

      <section className="reports-hero">
        <h1>My Reports</h1>
        <p>View and track all the reports you’ve submitted.</p>
        <p>Stay updated on their status and progress.</p>
      </section>

      <section className="filter-row">
        <select
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value)}
        >
          <option value="">Filter by status</option>
          <option value="PENDING">Pending</option>
          <option value="APPROVED">Approved</option>
          <option value="REJECTED">Rejected</option>
          <option value="IN_PROGRESS">In Progress</option>
          <option value="RESOLVED">Resolved</option>
        </select>
      </section>

      <section className="reports-grid">
        {filteredReports.length === 0 ? (
          <p className="no-reports">No reports found.</p>
        ) : (
          filteredReports.map((report) => (
            <div className="report-card" key={report.id}>
              <img
                src={photoUrls[report.id]}
                alt={report.title}
                className="report-img"
              />

              <div className="report-info">
                <span className={`status ${report.status.toLowerCase()}`}>
                  ● {report.status.replace("_", " ")}
                </span>

                <h2>{report.title}</h2>

                <p className="description">{report.description}</p>

                <p>Category: {report.category}</p>

                <p>Location: {report.locationName}</p>

                <p>Status: {report.status.replace("_", " ")}</p>

                {report.status === "PENDING" && (
                  <button
                    className="delete-report-btn"
                    onClick={() => deleteReport(report.id)}
                  >
                    Delete
                  </button>
                )}
              </div>
            </div>
          ))
        )}
      </section>

      <MobileBottomNav />
    </>
  );
}

export default MyReports;
