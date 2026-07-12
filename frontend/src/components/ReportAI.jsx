import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "./Navbar";
import MobileBottomNav from "./MobileBottomNav";
import "../css/reportAI.css";
import { apiFetch } from "../api/client";
import aiGif from "../assets/images/AI-desktop.gif";
import uploadIcon from "../assets/images/upload-icon.png";
import aiReportBG from "../assets/images/ai-reportBG1.png";

function ReportAI() {
  const navigate = useNavigate();

  const [photo, setPhoto] = useState(null);

  const [preview, setPreview] = useState("");

  const [loading, setLoading] = useState(false);

  const [aiGenerated, setAiGenerated] = useState(false);

  const [title, setTitle] = useState("");

  const [description, setDescription] = useState("");

  const [category, setCategory] = useState("");

  const [locationName, setLocationName] = useState("");

  const [latitude, setLatitude] = useState("");

  const [longitude, setLongitude] = useState("");

  const [locationDetected, setLocationDetected] = useState(false);

  function handlePhotoChange(event) {
    const file = event.target.files[0];

    if (!file) return;

    setPhoto(file);

    setPreview(URL.createObjectURL(file));
  }

  async function analyzeImage() {
    if (!photo) {
      alert("Please upload an image");

      return;
    }

    setLoading(true);

    const formData = new FormData();

    formData.append("photo", photo);

    try {
      const response = await apiFetch("/api/ai-reports/analyze", {
        method: "POST",

        body: formData,
      });

      if (!response.ok) {
        throw new Error("AI analysis failed");
      }

      const data = await response.json();

      setTitle(data.title || "");
      setDescription(data.description || "");
      setCategory(data.category || "");

      setLatitude(data.latitude || "");
      setLongitude(data.longitude || "");
      setLocationDetected(data.locationDetected);

      if (data.locationDetected && data.latitude && data.longitude) {
        getLocationName(data.longitude, data.latitude);
      } else {
        setLocationName(data.locationName || "");
      }

      setAiGenerated(true);
    } catch (error) {
      console.log(error);

      alert("AI analysis failed");
    } finally {
      setLoading(false);
    }
  }

  async function getLocationName(lng, lat) {
    try {
      const response = await fetch(
        `https://api.mapbox.com/geocoding/v5/mapbox.places/${lng},${lat}.json?access_token=${import.meta.env.VITE_MAPBOX_TOKEN}`,
      );

      const data = await response.json();

      if (data.features.length > 0) {
        setLocationName(data.features[0].place_name);
      }
    } catch (error) {
      console.log(error);
    }
  }

  function handleLocateMe() {
    if (!navigator.geolocation) {
      alert("Geolocation not supported");
      return;
    }

    navigator.geolocation.getCurrentPosition(
      (position) => {
        const lat = position.coords.latitude;
        const lng = position.coords.longitude;

        setLatitude(lat);
        setLongitude(lng);

        getLocationName(lng, lat);

        setLocationDetected(true);
      },

      () => {
        alert("Unable to get your location");
      },
    );
  }

  async function submitReport(event) {
    event.preventDefault();

    const formData = new FormData();

    formData.append("title", title);

    formData.append("description", description);

    formData.append("category", category);

    formData.append("locationName", locationName);

    formData.append("latitude", latitude);

    formData.append("longitude", longitude);

    formData.append("photo", photo);

    try {
      const response = await apiFetch("/api/reports", {
        method: "POST",

        body: formData,
      });

      if (!response.ok) {
        throw new Error("Failed to submit report");
      }

      alert("Report submitted successfully");

      navigate("/dashboard");
    } catch (error) {
      console.log(error);

      alert("Could not submit report");
    }
  }

  return (
    <>
      <Navbar />
      <section
        className="ai-hero"
        style={{ backgroundImage: `url(${aiReportBG})` }}
      >
        <div className="hero-overlay"></div>
        <div className="ai-gif-container">
          <img src={aiGif} alt="Nash AI Assistant" className="ai-gif" />
        </div>
      </section>
      <section className="ai-report-section">
        <h1>Hello, I'm Nash</h1>
        <p className="assistant-text">Your AI Reporting Assistant</p>
        <h2>Report an Incident with just one click!</h2>
        <div className="upload-form">
          <label htmlFor="incidentImage" className="upload-box">
            {preview ? (
              <img src={preview} alt="Preview" className="preview-image" />
            ) : (
              <div className="upload-content">
                <img src={uploadIcon} alt="Upload" />
                <p>Upload an Incident Image</p>
              </div>
            )}
          </label>
          <input
            type="file"
            id="incidentImage"
            accept="image/*"
            onChange={handlePhotoChange}
          />
          <button type="button" className="submit-btn" onClick={analyzeImage}>
            {loading ? "Analyzing..." : "Analyze Image"}
          </button>
        </div>

        {aiGenerated && (
          <form className="ai-result-form" onSubmit={submitReport}>
            <input
              type="text"
              placeholder="Title"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              required
            />
            <textarea
              placeholder="Description"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
            />
            <select
              value={category}
              onChange={(e) => setCategory(e.target.value)}
              required
            >
              <option value="">Select category</option>
              <option value="Road Damage">Road Damage</option>
              <option value="Waste / Cleanliness">Waste / Cleanliness</option>
              <option value="Street Light">Street Light</option>
              <option value="Water Leak">Water Leak</option>
              <option value="Public Safety">Public Safety</option>
              <option value="Other">Other</option>
            </select>
            <input
              type="text"
              placeholder="Location"
              value={locationName}
              onChange={(e) => setLocationName(e.target.value)}
              required
            />

            {!locationDetected && (
              <button
                type="button"
                className="submit-btn"
                onClick={handleLocateMe}
              >
                Locate Me
              </button>
            )}
            <button type="submit" className="submit-btn">
              Submit Report
            </button>
          </form>
        )}
      </section>
      <MobileBottomNav />
    </>
  );
}

export default ReportAI;
