import React, { useState } from "react";
import { addMember } from "../../api/client";

export default function MembersTab({ groupId, members, onMembersChanged }) {
  const [name, setName] = useState("");

  function handleAdd(event) {
    event.preventDefault();
    if (!name.trim()) return;
    addMember(groupId, name.trim()).then(() => {
      setName("");
      onMembersChanged();
    });
  }

  return (
    <div>
      <form onSubmit={handleAdd}>
        <input placeholder="Member name" value={name} onChange={(e) => setName(e.target.value)} />
        <button type="submit">Add member</button>
      </form>

      {members.length === 0 && <p className="empty-state">No members yet.</p>}

      {members.map((member) => (
        <div key={member.id} className="card">
          {member.name}
        </div>
      ))}
    </div>
  );
}
