import React, { useEffect, useState } from "react";
import { getSettlementPlan } from "../../api/client";
import { formatCents } from "../../utils/money";

export default function SettlementTab({ groupId, refreshSignal }) {
  const [transfers, setTransfers] = useState([]);

  useEffect(() => {
    getSettlementPlan(groupId).then(setTransfers);
  }, [groupId, refreshSignal]);

  if (transfers.length === 0) {
    return <p className="empty-state">Nothing to settle. Everyone's square.</p>;
  }

  return (
    <div>
      <p style={{ color: "var(--text-dim)" }}>
        The fewest payments that clear every balance in the group:
      </p>
      {transfers.map((transfer, index) => (
        <div key={index} className="card">
          <strong>{transfer.fromName}</strong> pays <strong>{transfer.toName}</strong>{" "}
          {formatCents(transfer.amountCents)}
        </div>
      ))}
    </div>
  );
}
