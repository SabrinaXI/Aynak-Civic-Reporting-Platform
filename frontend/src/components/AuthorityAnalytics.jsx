import { useEffect, useState } from "react";

import Navbar from "./Navbar";

import "../css/authorityAnalytics.css";
import { apiFetch } from "../api/client";

import {
  ResponsiveContainer,
  BarChart,
  Bar,
  PieChart,
  Pie,
  Cell,
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  CartesianGrid,
} from "recharts";

function AuthorityAnalytics() {
  const [categoryData, setCategoryData] = useState([]);

  const [statusData, setStatusData] = useState([]);

  const [locationData, setLocationData] = useState([]);

  const [timeData, setTimeData] = useState([]);

  useEffect(() => {
    apiFetch("/api/reports/analytics/category")
      .then((res) => res.json())

      .then((data) => {
        setCategoryData(
          Object.entries(data).map(([name, value]) => ({ name, value })),
        );
      });

    apiFetch("/api/reports/analytics/status")
      .then((res) => res.json())

      .then((data) => {
        setStatusData(
          Object.entries(data).map(([name, value]) => ({ name, value })),
        );
      });

    apiFetch("/api/reports")
      .then((res) => res.json())

      .then((reports) => {
        buildLocationData(reports);

        buildTimeData(reports);
      });
  }, []);

  function buildLocationData(reports) {
    const locationCounts = {};

    reports.forEach((report) => {
      const location = report.locationName || "Unknown Location";

      const shortLocation = location.split(",")[0];

      locationCounts[shortLocation] = (locationCounts[shortLocation] || 0) + 1;
    });

    const formatted = Object.entries(locationCounts)

      .map(([name, value]) => ({ name, value }))

      .sort((a, b) => b.value - a.value)

      .slice(0, 6);

    setLocationData(formatted);
  }

  function buildTimeData(reports) {
    const dateCounts = {};

    reports.forEach((report) => {
      if (report.createdAt) {
        const date = report.createdAt.substring(0, 10);

        dateCounts[date] = (dateCounts[date] || 0) + 1;
      }
    });

    const formatted = Object.entries(dateCounts)

      .map(([date, value]) => ({ date, value }))

      .sort((a, b) => new Date(a.date) - new Date(b.date));

    setTimeData(formatted);
  }

  const PIE_COLORS = [
    "#4F46E5",

    "#9333EA",

    "#2563EB",

    "#22C55E",

    "#F59E0B",

    "#EF4444",
  ];

  return (
    <>
      <Navbar />
      <main className="analytics-page">
        <section className="analytics-hero">
          <h1>Community Analytics</h1>
          <p>
            Monitor reporting trends, incident categories, high-report areas,
            and report progress across the UAE.
          </p>
        </section>
        <section className="analytics-grid">
          <div className="chart-card">
            <h2>Reports by Category</h2>
            <ResponsiveContainer width="100%" height={190}>
              <BarChart data={categoryData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip />
                <Bar dataKey="value" fill="#42ff8c" radius={[8, 8, 0, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
          <div className="chart-card">
            <h2>Reports by Status</h2>
            <ResponsiveContainer width="100%" height={190}>
              <PieChart>
                <Pie
                  data={statusData}
                  dataKey="value"
                  nameKey="name"
                  outerRadius={65}
                  label
                >
                  {statusData.map((entry, index) => (
                    <Cell
                      key={index}
                      fill={PIE_COLORS[index % PIE_COLORS.length]}
                    />
                  ))}
                </Pie>
                <Tooltip />
                <Legend />
              </PieChart>
            </ResponsiveContainer>
          </div>
          <div className="chart-card">
            <h2>Most Reported Areas</h2>
            <ResponsiveContainer width="100%" height={190}>
              <BarChart data={locationData} layout="vertical">
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis type="number" />
                <YAxis dataKey="name" type="category" width={120} />
                <Tooltip />
                <Bar dataKey="value" fill="#2563EB" radius={[0, 8, 8, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
          <div className="chart-card">
            <h2>Reports Over Time</h2>
            <ResponsiveContainer width="100%" height={190}>
              <LineChart data={timeData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="date" />
                <YAxis />
                <Tooltip />
                <Line
                  type="monotone"
                  dataKey="value"
                  stroke="#9333EA"
                  strokeWidth={3}
                  dot={{ r: 4 }}
                />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </section>
      </main>
    </>
  );
}

export default AuthorityAnalytics;
