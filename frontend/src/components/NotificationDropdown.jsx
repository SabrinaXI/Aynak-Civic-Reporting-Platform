import { useEffect, useState } from "react";

import "../css/notificationDropdown.css";

import notificationBell from "../assets/images/notificationbell.png";

import { apiFetch } from "../api/client";

function NotificationDropdown() {
  const [notifications, setNotifications] = useState([]);

  const [showNotifications, setShowNotifications] = useState(false);

  const unreadCount = notifications.filter(
    (notification) => notification.readStatus === false,
  ).length;

  useEffect(() => {
    loadNotifications();
  }, []);

  function loadNotifications() {
    apiFetch("/api/notifications/my")
      .then((response) => response.json())

      .then((data) => setNotifications(data))

      .catch((error) => console.log(error));
  }

  function handleBellClick() {
    setShowNotifications(!showNotifications);

    apiFetch("/api/notifications/my/read", {
      method: "PUT",
    })
      .then(() => loadNotifications())

      .catch((error) => console.log(error));
  }

  return (
    <div className="notification-wrapper">
      <div className="icon-circle" onClick={handleBellClick}>
        <img src={notificationBell} alt="Notifications" />

        {unreadCount > 0 && (
          <span className="notification-badge">{unreadCount}</span>
        )}
      </div>

      {showNotifications && (
        <div className="notification-dropdown">
          <h3>Notifications</h3>

          {notifications.length === 0 ? (
            <p className="no-notifications">No notifications yet.</p>
          ) : (
            notifications.map((notification) => (
              <div className="notification-item" key={notification.id}>
                <p>{notification.message}</p>
                <span>
                  {notification.createdAt
                    ? notification.createdAt.substring(0, 10)
                    : ""}
                </span>
              </div>
            ))
          )}
        </div>
      )}
    </div>
  );
}

export default NotificationDropdown;
