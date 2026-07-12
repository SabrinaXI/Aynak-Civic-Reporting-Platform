import { useState } from "react";
import Navbar from "./Navbar";
import "../css/createVoucher.css";
import { apiFetch } from "../api/client";

function CreateVoucher() {
  const [brandName, setBrandName] = useState("");
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [voucherCode, setVoucherCode] = useState("");
  const [pointsRequired, setPointsRequired] = useState("");

  function handleSubmit(event) {
    event.preventDefault();

    const formData = new FormData();

    formData.append("brandName", brandName);
    formData.append("title", title);
    formData.append("description", description);
    formData.append("voucherCode", voucherCode);
    formData.append("pointsRequired", pointsRequired);

    apiFetch("/api/rewards/sponsor/create", {
      method: "POST",
      body: formData,
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to create voucher");
        }

        return response.json();
      })
      .then(() => {
        alert("Voucher created successfully!");

        setBrandName("");
        setTitle("");
        setDescription("");
        setVoucherCode("");
        setPointsRequired("");
      })
      .catch((error) => {
        console.log(error);
        alert("Something went wrong");
      });
  }

  return (
    <>
      <Navbar />

      <main className="create-voucher-page">
        <section className="create-voucher-hero">
          <h1>Create Voucher</h1>
          <p>
            Add a new reward voucher that citizens can redeem using Aynak
            Points.
          </p>
        </section>

        <section className="voucher-form-section">
          <form className="voucher-form" onSubmit={handleSubmit}>
            <label>Brand Name</label>
            <input
              type="text"
              placeholder="Example: CARREFOUR"
              value={brandName}
              onChange={(e) => setBrandName(e.target.value)}
              required
            />

            <label>Voucher Title</label>
            <input
              type="text"
              placeholder="Example: 20 AED Discount"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              required
            />

            <label>Description</label>
            <textarea
              placeholder="Example: Redeem this voucher for a discount."
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              required
            ></textarea>

            <label>Voucher Code</label>
            <input
              type="text"
              placeholder="Example: AYN-2026-100"
              value={voucherCode}
              onChange={(e) => setVoucherCode(e.target.value)}
              required
            />

            <label>Points Required</label>
            <input
              type="number"
              placeholder="Example: 100"
              value={pointsRequired}
              onChange={(e) => setPointsRequired(e.target.value)}
              required
            />

            <button type="submit">Create Voucher</button>
          </form>
        </section>
      </main>
    </>
  );
}

export default CreateVoucher;