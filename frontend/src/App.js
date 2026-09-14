import React from "react";
import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import GroupListPage from "./pages/GroupListPage";
import GroupDetailPage from "./pages/GroupDetailPage";

export default function App() {
  return (
    <BrowserRouter>
      <header className="app-header">
        <Link to="/" className="app-title">
          tallyup
        </Link>
      </header>
      <main className="app-main">
        <Routes>
          <Route path="/" element={<GroupListPage />} />
          <Route path="/groups/:groupId" element={<GroupDetailPage />} />
        </Routes>
      </main>
    </BrowserRouter>
  );
}
