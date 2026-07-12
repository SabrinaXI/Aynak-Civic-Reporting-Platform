import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import mapboxgl from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";
import Navbar from "./Navbar";
import MobileBottomNav from "./MobileBottomNav";
import "../css/report.css";
import { apiFetch } from "../api/client";

mapboxgl.accessToken = import.meta.env.VITE_MAPBOX_TOKEN;

function Report() {
  const navigate = useNavigate();
  const mapContainer = useRef(null);
  const map = useRef(null);
  const marker = useRef(null);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [category, setCategory] = useState("");
  const [locationName, setLocationName] = useState("");
  const [photo, setPhoto] = useState(null);
  const [latitude, setLatitude] = useState("");
  const [longitude, setLongitude] = useState("");
  const [photoError, setPhotoError] = useState("");


  //reverse geocoding
  async function getLocationName(lng, lat) {
    try {
      const response = await fetch(
        `https://api.mapbox.com/geocoding/v5/mapbox.places/${lng},${lat}.json?access_token=${mapboxgl.accessToken}`,
      );
      const data = await response.json();
      if (data.features.length > 0) {
        setLocationName(data.features[0].place_name);
      }
    } catch (error) {
      console.log(error);
    }
  }


  //function to place the marker on the map
  function placeMarker(lng, lat) {
    setLongitude(lng);
    setLatitude(lat);

    getLocationName(lng,lat);

    if (marker.current) {
      marker.current.remove();
    }

    marker.current = new mapboxgl.Marker()
      .setLngLat([lng, lat])
      .addTo(map.current);
  }


  useEffect(() => {
    if (map.current) {
      map.current.remove();
    }

    map.current = new mapboxgl.Map({
      container: mapContainer.current,
      style: "mapbox://styles/mapbox/satellite-streets-v12",
      center: [54.3773, 24.4539],
      zoom: 10,
    });

    map.current.on("click", (e) => {
      placeMarker(e.lngLat.lng, e.lngLat.lat);
    });

    return () => {
      map.current?.remove();
    };
  }, []);


  //locate me function
  function handleLocateMe() {
    if (!navigator.geolocation) {
      alert("Geolocation is not supported by your browser");
      return;
    }

    navigator.geolocation.getCurrentPosition(
      (position) => {
        const lat = position.coords.latitude;
        const lng = position.coords.longitude;

        placeMarker(lng, lat);

        map.current.flyTo({
          center: [lng, lat],

          zoom: 15,
        });
      },

      () => {
        alert("Unable to get your location");
      },
    );
  }

  //function when user clicks submit form
  function handleSubmit(event) {
    event.preventDefault();

    if (!photo) {
      setPhotoError("Please submit a photo with the incident");
      return;
    }
    setPhotoError("");

    if (!latitude || !longitude) {
      alert("Please select a location on the map or use Locate Me");
      return;
    }

    const formData = new FormData();
    formData.append("title", title);
    formData.append("description", description);
    formData.append("category", category);
    formData.append("locationName", locationName);
    formData.append("latitude", latitude);
    formData.append("longitude", longitude);
    formData.append("photo", photo);

    apiFetch("/api/reports", {
      method: "POST",
      body: formData,
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to submit report");
        }
        return response.text();
      })

      .then((message) => {
        alert(message);
        navigate("/dashboard");
      })

      .catch((error) => {
        console.log(error);
        alert("Something went wrong");
      });
  }

  return (
    <>
      <Navbar />
      <div className="report-page">
        <div className="report-left">
          <h1>Select Incident Location</h1>
          <div className="location-instruction-row">
            <p>Click on the map to choose a location or use Locate Me.</p>
            <button type="button" id="locateBtn" onClick={handleLocateMe}>
              Locate Me
            </button>
          </div>
          <div ref={mapContainer} id="map"></div>
        </div>
        <div className="report-form-container">
          <form className="report-form" onSubmit={handleSubmit}>
            <label>Title</label>
            <input
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              required
            />
            <label>Description</label>
            <textarea
              rows="3"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
            ></textarea>
            <label>Category</label>
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
            <label>Location</label>
            <input
              type="text"
              placeholder="Enter location name"
              value={locationName}
              onChange={(e) => setLocationName(e.target.value)}
              required
            />
            <label>Upload Photo</label>
            <input
              type="file"
              accept="image/*"
              className="upload-input"
              onChange={(e) => {
                setPhoto(e.target.files[0]);

                setPhotoError("");
              }}
            />

            {photoError && <div className="photo-error">{photoError}</div>}
            <button type="submit">SUBMIT REPORT</button>
          </form>
        </div>
      </div>
      <MobileBottomNav />
    </>
  );
}

export default Report;
