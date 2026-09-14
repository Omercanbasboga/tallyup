import React, { useCallback, useEffect, useState } from "react";
import { addExpense, listExpenses } from "../../api/client";
import { formatCents, toCents } from "../../utils/money";

export default function ExpensesTab({ groupId, members, onExpenseAdded }) {
  const [expenses, setExpenses] = useState([]);
  const [description, setDescription] = useState("");
  const [amount, setAmount] = useState("");
  const [paidByMemberId, setPaidByMemberId] = useState(members[0]?.id ?? "");
  const [participantIds, setParticipantIds] = useState(members.map((m) => m.id));

  const refresh = useCallback(() => {
    listExpenses(groupId).then(setExpenses);
  }, [groupId]);

  useEffect(() => {
    refresh();
  }, [refresh]);

  useEffect(() => {
    if (!paidByMemberId && members.length > 0) {
      setPaidByMemberId(members[0].id);
    }
    setParticipantIds(members.map((m) => m.id));
    // members list only grows here, so re-deriving on every change is fine
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [members]);

  function toggleParticipant(memberId) {
    setParticipantIds((current) =>
      current.includes(memberId) ? current.filter((id) => id !== memberId) : [...current, memberId]
    );
  }

  function handleAdd(event) {
    event.preventDefault();
    if (!description.trim() || !amount || participantIds.length === 0) return;

    addExpense(groupId, {
      description: description.trim(),
      amountCents: toCents(amount),
      paidByMemberId,
      participantMemberIds: participantIds,
    }).then(() => {
      setDescription("");
      setAmount("");
      refresh();
      onExpenseAdded();
    });
  }

  if (members.length === 0) {
    return <p className="empty-state">Add a member first, then you can log expenses.</p>;
  }

  return (
    <div>
      <form onSubmit={handleAdd} style={{ flexDirection: "column" }}>
        <div style={{ display: "flex", gap: "0.5rem" }}>
          <input
            placeholder="What was it for?"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
          <input
            placeholder="Amount, e.g. 42.50"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
          />
        </div>

        <div>
          <label>Paid by: </label>
          <select value={paidByMemberId} onChange={(e) => setPaidByMemberId(Number(e.target.value))}>
            {members.map((m) => (
              <option key={m.id} value={m.id}>
                {m.name}
              </option>
            ))}
          </select>
        </div>

        <div className="checkbox-list">
          <p style={{ margin: "0.4rem 0", color: "var(--text-dim)" }}>Split between:</p>
          {members.map((m) => (
            <label key={m.id}>
              <input
                type="checkbox"
                checked={participantIds.includes(m.id)}
                onChange={() => toggleParticipant(m.id)}
              />
              {" " + m.name}
            </label>
          ))}
        </div>

        <button type="submit">Add expense</button>
      </form>

      {expenses.length === 0 && <p className="empty-state">No expenses logged yet.</p>}

      {expenses.map((expense) => (
        <div key={expense.id} className="card">
          <h3>{expense.description}</h3>
          <p style={{ color: "var(--text-dim)", margin: 0 }}>{formatCents(expense.amountCents)}</p>
        </div>
      ))}
    </div>
  );
}
