import axios from "axios";

const client = axios.create({
  baseURL: process.env.REACT_APP_API_BASE_URL || "http://localhost:8083",
});

export const listGroups = () => client.get("/api/groups").then((r) => r.data);
export const createGroup = (name) => client.post("/api/groups", { name }).then((r) => r.data);
export const getGroup = (groupId) => client.get(`/api/groups/${groupId}`).then((r) => r.data);

export const listMembers = (groupId) => client.get(`/api/groups/${groupId}/members`).then((r) => r.data);
export const addMember = (groupId, name) =>
  client.post(`/api/groups/${groupId}/members`, { name }).then((r) => r.data);

export const listExpenses = (groupId) => client.get(`/api/groups/${groupId}/expenses`).then((r) => r.data);
export const addExpense = (groupId, expense) =>
  client.post(`/api/groups/${groupId}/expenses`, expense).then((r) => r.data);

export const getBalances = (groupId) => client.get(`/api/groups/${groupId}/balances`).then((r) => r.data);
export const getSettlementPlan = (groupId) =>
  client.get(`/api/groups/${groupId}/settlement`).then((r) => r.data);

export default client;
