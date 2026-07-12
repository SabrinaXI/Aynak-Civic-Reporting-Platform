import { useEffect, useRef, useState } from "react";
import mapboxgl from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";
import Navbar from "./Navbar";
import "../css/dashboard.css";
import MobileBottomNav from "./MobileBottomNav";
import { apiFetch } from "../api/client";

mapboxgl.accessToken = import.meta.env.VITE_MAPBOX_TOKEN;

function Dashboard() {
  const mapContainer = useRef(null);
  const map = useRef(null);
  const [userData, setUserData] = useState(null);
  const [reports, setReports] = useState([]);
  const [filteredReports, setFilteredReports] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [category, setCategory] = useState("");
  const [city, setCity] = useState("");
  const [myReports, setMyReports] = useState([]);

  // ================= USER =================
  useEffect(() => {
    apiFetch("/api/users/me")
      .then((response) => response.json())
      .then((data) => setUserData(data))
      .catch((error) => console.log(error));
  }, []);

  // ================= REPORTS =================
  useEffect(() => {
    apiFetch("/api/reports/my")
      .then((response) => response.json())

      .then((data) => {
        setMyReports(data);
      })

      .catch((error) => console.log(error));
  }, []);

// all reports
  useEffect(() => {
    apiFetch("/api/reports")
      .then((response) => response.json())
      .then((data) => {
        const latestReports = [...data].reverse().slice(0, 10);

        setReports(latestReports);

        setFilteredReports(latestReports);
      })

      .catch((error) => console.log(error));
  }, []);

  // ================= MAP =================
  useEffect(() => {
    if (map.current) {
      map.current.remove();
    }

    map.current = new mapboxgl.Map({
      container: mapContainer.current,
      style: "mapbox://styles/mapbox/dark-v11",
      center: [54.3773, 24.4539],
      zoom: 10,
    });

    //map.current.addControl(new mapboxgl.NavigationControl());

    reports.forEach((report) => {
      if (report.latitude && report.longitude) {
        new mapboxgl.Marker()
          .setLngLat([report.longitude, report.latitude])
          .setPopup(
            new mapboxgl.Popup().setHTML(`
              <h3>${report.title}</h3>
              <p>${report.category}</p>
              <p>${report.locationName}</p>
              <p>Status: ${report.status}</p>
            `),
          )
          .addTo(map.current);
      }
    }); //{offset:25}

    return () => {
      map.current?.remove();
    };
  }, [reports]);

  // ================= FILTERS =================
  function applyFilters() {
    let result = [...reports];

    if (searchTerm.trim() !== "") {
      result = result.filter((report) =>
        report.title?.toLowerCase().includes(searchTerm.toLowerCase()),
      );
    }

    if (category !== "") {
      result = result.filter((report) => report.category === category);
    }

    if (city !== "") {
      result = result.filter((report) =>
        report.locationName?.toLowerCase().includes(city.toLowerCase()),
      );
    }

    setFilteredReports(result);
  }

  function clearFilters() {
    setSearchTerm("");
    setCategory("");
    setCity("");
    setFilteredReports(reports);
  }

  return (
    <>
      <Navbar />

      {/* HERO */}
      <section className="hero-section">
        <div className="hero-overlay"></div>
        <div className="hero-content">
          <h1>Hello, {userData?.firstName || "Citizen"}</h1>
          <p>You have {myReports.length} reports</p>
          <p>You have {userData?.aynakPoints || 0} Aynak Points</p>
        </div>
      </section>

      {/* FILTER */}
      <section className="filter-section">
        <div className="filter-top">
          <div className="search-box">
            <input
              type="text"
              placeholder="Search by title"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
          <div className="category-box">
            <select
              value={category}
              onChange={(e) => setCategory(e.target.value)}
            >
              <option value="">Filter by category</option>
              <option value="Road Damage">Road Damage</option>
              <option value="Waste / Cleanliness">Waste / Cleanliness</option>
              <option value="Street Light">Street Light</option>
              <option value="Water Leak">Water Leak</option>
              <option value="Public Safety">Public Safety</option>
              <option value="Other">Other</option>
            </select>
          </div>
        </div>
        <div className="cities-container">
          <label>
            <input
              type="radio"
              name="city"
              value="Abu Dhabi"
              checked={city === "Abu Dhabi"}
              onChange={(e) => setCity(e.target.value)}
            />
            Abu Dhabi
          </label>
          <label>
            <input
              type="radio"
              name="city"
              value="Dubai"
              checked={city === "Dubai"}
              onChange={(e) => setCity(e.target.value)}
            />
            Dubai
          </label>
          <label>
            <input
              type="radio"
              name="city"
              value="Sharjah"
              checked={city === "Sharjah"}
              onChange={(e) => setCity(e.target.value)}
            />
            Sharjah
          </label>
          <label>
            <input
              type="radio"
              name="city"
              value="Ajman"
              checked={city === "Ajman"}
              onChange={(e) => setCity(e.target.value)}
            />
            Ajman
          </label>
          <label>
            <input
              type="radio"
              name="city"
              value="Fujairah"
              checked={city === "Fujairah"}
              onChange={(e) => setCity(e.target.value)}
            />
            Fujairah
          </label>

          <label>
            <input
              type="radio"
              name="city"
              value="Ras Al Khaimah"
              checked={city === "Ras Al Khaimah"}
              onChange={(e) => setCity(e.target.value)}
            />
            Ras Al Khaimah
          </label>
          <label>
            <input
              type="radio"
              name="city"
              value="Umm Al Quwain"
              checked={city === "Umm Al Quwain"}
              onChange={(e) => setCity(e.target.value)}
            />
            Umm Al Quwain
          </label>
        </div>
        <div className="filter-buttons">
          <button id="applyBtn" onClick={applyFilters}>
            Apply Filters
          </button>
          <button className="clear-btn" onClick={clearFilters}>
            Clear
          </button>
        </div>
      </section>

      {/* MAP + POSTS */}
      <section className="dashboard-content">
        <div className="map-panel">
          <h2>Map</h2>
          <div className="map-wrapper">
            <div ref={mapContainer} id="dashboardMap"></div>
          </div>
        </div>
        <div className="posts-panel">
          <h2>Recent Reports</h2>
          <div className="posts-container">
            {filteredReports.length === 0 ? (
              <p>No reports found.</p>
            ) : (
              filteredReports.map((report) => (
                <div key={report.id} className="post-card">
                  <div className="post-left">
                    <h2>{report.title}</h2>
                    <p>{report.description}</p>
                    <div className="post-info">
                      <span>{report.locationName}</span>
                      <span>{report.status}</span>
                    </div>
                  </div>
                  <div className="post-right">
                    <button className="read-more-btn">Read More</button>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>
      </section>

      <MobileBottomNav />
    </>
  );
}

export default Dashboard;
