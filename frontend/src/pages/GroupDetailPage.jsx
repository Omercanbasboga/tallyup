import React, { useCallback, useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import { getGroup, listMembers } from "../api/client";
import MembersTab from "./tabs/MembersTab";
import ExpensesTab from "./tabs/ExpensesTab";
import BalancesTab from "./tabs/BalancesTab";
import SettlementTab from "./tabs/SettlementTab";

const TABS = ["Members", "Expenses", "Balances", "Settlement"];

export default function GroupDetailPage() {
  const { groupId } = useParams();
  const [group, setGroup] = useState(null);
  const [members, setMembers] = useState([]);
  const [activeTab, setActiveTab] = useState("Members");
  const [refreshSignal, setRefreshSignal] = useState(0);

  const refreshMembers = useCallback(() => {
    listMembers(groupId).then(setMembers);
  }, [groupId]);

  useEffect(() => {
    getGroup(groupId).then(setGroup);
    refreshMembers();
  }, [groupId, refreshMembers]);

  function bumpRefreshSignal() {
    setRefreshSignal((n) => n + 1);
  }

  if (!group) {
    return <p className="empty-state">loading...</p>;
  }

  return (
    <div>
      <p>
        <Link to="/">&larr; all groups</Link>
      </p>
      <h2>{group.name}</h2>

      <div className="tabs">
        {TABS.map((tab) => (
          <button
            key={tab}
            className={`tab-button ${activeTab === tab ? "active" : ""}`}
            onClick={() => setActiveTab(tab)}
          >
            {tab}
          </button>
        ))}
      </div>

      {activeTab === "Members" && (
        <MembersTab
          groupId={groupId}
          members={members}
          onMembersChanged={() => {
            refreshMembers();
            bumpRefreshSignal();
          }}
        />
      )}

      {activeTab === "Expenses" && (
        <ExpensesTab groupId={groupId} members={members} onExpenseAdded={bumpRefreshSignal} />
      )}

      {activeTab === "Balances" && <BalancesTab groupId={groupId} refreshSignal={refreshSignal} />}

      {activeTab === "Settlement" && <SettlementTab groupId={groupId} refreshSignal={refreshSignal} />}
    </div>
  );
}
