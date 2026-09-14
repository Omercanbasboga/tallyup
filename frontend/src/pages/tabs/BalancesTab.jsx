import React, { useEffect, useState } from "react";
import { getBalances } from "../../api/client";
import { formatCents } from "../../utils/money";

export default function BalancesTab({ groupId, refreshSignal }) {
  const [balances, setBalances] = useState([]);

  useEffect(() => {
    getBalances(groupId).then(setBalances);
  }, [groupId, refreshSignal]);

  const nonZero = balances.filter((b) => b.amountCents !== 0);

  if (nonZero.length === 0) {
    return <p className="empty-state">Everyone's even. Log an expense to see balances here.</p>;
  }

  return (
    <div>
      {nonZero.map((balance) => (
        <div key={balance.memberId} className="card">
          <span>{balance.memberName}</span>{" "}
          <span className={balance.amountCents > 0 ? "balance-positive" : "balance-negative"}>
            {balance.amountCents > 0
              ? `is owed ${formatCents(balance.amountCents)}`
              : `owes ${formatCents(Math.abs(balance.amountCents))}`}
          </span>
        </div>
      ))}
    </div>
  );
}
