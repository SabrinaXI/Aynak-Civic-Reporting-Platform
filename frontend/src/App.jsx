import { Routes, Route } from "react-router-dom";
import Landing from "./components/Landing";
import Dashboard from "./components/Dashboard";
import Report from "./components/Report";
import MyReports from "./components/MyReports";
import Profile from "./components/Profile";
import Leaderboard from "./components/Leaderboard";
import Rewards from "./components/Rewards";
import ReportAI from "./components/ReportAI";
import NotFound from "./components/NotFound";
import ProtectedRoute from "./routes/ProtectedRoute";
import AuthorityDashboard from "./components/AuthorityDashboard";
import ApprovedReports from "./components/ApprovedReports";
import AuthorityAnalytics from "./components/AuthorityAnalytics";
import AuthorityProfile from "./components/AuthorityProfile";
import SponsorDashboard from "./components/SponsorDashboard";
import CreateVoucher from "./components/CreateVoucher";

function App() {
  return (
    <Routes>
      <Route path="/" element={<Landing />} />

      <Route
        path="/dashboard"
        element={
          <ProtectedRoute allowedRoles={["CITIZEN", "ADMIN"]}>
            <Dashboard />
          </ProtectedRoute>
        }
      />

      <Route
        path="/report"
        element={
          <ProtectedRoute allowedRoles={["CITIZEN", "ADMIN"]}>
            <Report />
          </ProtectedRoute>
        }
      />

      <Route
        path="/report-ai"
        element={
          <ProtectedRoute allowedRoles={["CITIZEN", "ADMIN"]}>
            <ReportAI />
          </ProtectedRoute>
        }
      />

      <Route
        path="/my-reports"
        element={
          <ProtectedRoute allowedRoles={["CITIZEN", "ADMIN"]}>
            <MyReports />
          </ProtectedRoute>
        }
      />

      <Route
        path="/rewards"
        element={
          <ProtectedRoute allowedRoles={["CITIZEN", "ADMIN"]}>
            <Rewards />
          </ProtectedRoute>
        }
      />

      <Route
        path="/profile"
        element={
          <ProtectedRoute allowedRoles={["CITIZEN", "ADMIN"]}>
            <Profile />
          </ProtectedRoute>
        }
      />

      <Route
        path="/leaderboard"
        element={
          <ProtectedRoute allowedRoles={["CITIZEN", "ADMIN"]}>
            <Leaderboard />
          </ProtectedRoute>
        }
      />

      <Route
        path="/authority-dashboard"
        element={
          <ProtectedRoute allowedRoles={["AUTHORITY"]}>
            <AuthorityDashboard />
          </ProtectedRoute>
        }
      />
      <Route
        path="/approved-reports"
        element={
          <ProtectedRoute allowedRoles={["AUTHORITY"]}>
            <ApprovedReports />
          </ProtectedRoute>
        }
      />
      <Route
        path="/authority-analytics"
        element={
          <ProtectedRoute allowedRoles={["AUTHORITY"]}>
            <AuthorityAnalytics />
          </ProtectedRoute>
        }
      />
      <Route
        path="/authority-profile"
        element={
          <ProtectedRoute allowedRoles={["AUTHORITY"]}>
            <AuthorityProfile />
          </ProtectedRoute>
        }
      />

      <Route
        path="/sponsor-dashboard"
        element={
          <ProtectedRoute allowedRoles={["SPONSOR"]}>
            <SponsorDashboard />
          </ProtectedRoute>
        }
      />

      <Route
        path="/create-voucher"
        element={
          <ProtectedRoute allowedRoles={["SPONSOR"]}>
            <CreateVoucher />
          </ProtectedRoute>
        }
      />

      <Route path="*" element={<NotFound />} />
    </Routes>
  );
}

export default App;
