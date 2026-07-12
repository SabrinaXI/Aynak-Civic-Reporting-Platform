import { useEffect, useState } from "react";

import Navbar from "./Navbar";

import "../css/approvedReports.css";
import { apiFetch } from "../api/client";

function ApprovedReports() {
  const [reports, setReports] = useState([]);

  useEffect(() => {
    loadReports();
  }, []);

  function viewPhoto(id) {
    apiFetch(`/api/reports/photo/${id}`)
      .then((response) => response.blob())
      .then((blob) => {
        window.open(URL.createObjectURL(blob), "_blank", "noreferrer");
      })
      .catch((error) => console.log(error));
  }

  function loadReports() {
    apiFetch("/api/reports")
      .then((response) => response.json())

      .then((data) => {
        const actionReports = data.filter(
          (report) =>
            report.status === "APPROVED" ||
            report.status === "IN_PROGRESS" ||
            report.status === "RESOLVED",
        );

        setReports(actionReports);
      })

      .catch((error) => console.log(error));
  }

  function markInProgress(id) {
    apiFetch(`/api/reports/authority/${id}/in-progress`, {
      method: "PUT",
    })
      .then((response) => {
        if (!response.ok) throw new Error("Failed to update report");

        return response.text();
      })

      .then(() => {
        alert("Report marked as In Progress");

        loadReports();
      })

      .catch((error) => {
        console.log(error);

        alert("Something went wrong");
      });
  }

  function markResolved(id) {
    apiFetch(`/api/reports/authority/${id}/resolved`, {
      method: "PUT",
    })
      .then((response) => {
        if (!response.ok) throw new Error("Failed to resolve report");

        return response.text();
      })

      .then(() => {
        alert("Report marked as Resolved");

        loadReports();
      })

      .catch((error) => {
        console.log(error);

        alert("Something went wrong");
      });
  }

  return (
    <>
      <Navbar />
      <main className="approved-page">
        <section className="approved-hero">
          <h1>Approved Reports</h1>
          <p>
            Approved reports are ready for action!
          </p>
        </section>
        <section className="approved-table-section">
          <h2>Reports Ready for Action</h2>
          <div className="approved-table-wrapper">
            <table className="approved-table">
              <thead>
                <tr>
                  <th>Report ID</th>
                  <th>Title</th>
                  <th>Category</th>
                  <th>Location</th>
                  <th>Status</th>
                  <th>Submitted On</th>
                  <th>Image</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {reports.length === 0 ? (
                  <tr>
                    <td colSpan="8" className="empty-row">
                      No approved reports yet.
                    </td>
                  </tr>
                ) : (
                  reports.map((report) => (
                    <tr key={report.id}>
                      <td>{report.id}</td>
                      <td>{report.title}</td>
                      <td>{report.category}</td>
                      <td>{report.locationName}</td>
                      <td>{report.status.replace("_", " ")}</td>
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
                        {report.status === "APPROVED" && (
                          <button
                            className="progress-btn"
                            onClick={() => markInProgress(report.id)}
                          >
                            Mark In Progress
                          </button>
                        )}

                        {report.status === "IN_PROGRESS" && (
                          <button
                            className="resolved-btn"
                            onClick={() => markResolved(report.id)}
                          >
                            Mark Resolved
                          </button>
                        )}

                        {report.status === "RESOLVED" && (
                          <span className="resolved-text">Resolved</span>
                        )}
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

export default ApprovedReports;
