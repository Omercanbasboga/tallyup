import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { listGroups, createGroup } from "../api/client";

export default function GroupListPage() {
  const [groups, setGroups] = useState([]);
  const [newGroupName, setNewGroupName] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    refresh();
  }, []);

  function refresh() {
    listGroups()
      .then(setGroups)
      .finally(() => setLoading(false));
  }

  function handleCreate(event) {
    event.preventDefault();
    if (!newGroupName.trim()) return;
    createGroup(newGroupName.trim()).then(() => {
      setNewGroupName("");
      refresh();
    });
  }

  return (
    <div>
      <h2>Your groups</h2>

      <form onSubmit={handleCreate}>
        <input
          placeholder="e.g. Cabin trip"
          value={newGroupName}
          onChange={(e) => setNewGroupName(e.target.value)}
        />
        <button type="submit">Create group</button>
      </form>

      {loading && <p className="empty-state">loading...</p>}

      {!loading && groups.length === 0 && (
        <p className="empty-state">No groups yet. Make one above.</p>
      )}

      {groups.map((group) => (
        <Link key={group.id} to={`/groups/${group.id}`} className="card" style={{ display: "block" }}>
          <h3>{group.name}</h3>
        </Link>
      ))}
    </div>
  );
}
