# tallyup

A group expense tracker with a real settlement algorithm behind it, not just a running total of who paid what.

You add expenses to a group (who paid, how much, who it was split between), and tallyup keeps a running balance for each person. The interesting part is the settlement page: instead of showing you a tangled list of everyone owing everyone, it works out the shortest list of actual payments that clears every balance at once.

## Why the settlement part matters

Say four people split a trip. A fronted the hotel, B fronted the car, C paid for dinner one night, D paid for another. By the end everyone's owed something and owes something else. If you just show raw pairwise debts, you get a mess: eight or nine "X owes Y" lines, half of which cancel out if you actually look at them together.

tallyup nets everyone's balance first, then greedily matches whoever's owed the most against whoever owes the most, over and over, until the group is settled. For most real groups that gets close to the minimum number of payments possible. Finding the actual mathematical minimum in every case is a harder problem than it looks (it reduces to something partition-like), so this is the standard practical approach, not a from-first-principles proof of optimality.

Money is handled in cents the whole way through, never as a double. Splitting a $10 bill three ways as a float gives you 3.3333333333333335 per person and rounding errors that don't add back up to $10. Doing it in integer cents with a proper remainder-distribution rule means the split always sums back to the original amount, down to the cent.

## Structure

```
backend/    Spring Boot API - groups, members, expenses, balances, settlement plan
frontend/   React app - talks to the API above
```

## Running it

Backend:

```bash
cd backend
./mvnw spring-boot:run
```

Starts on `:8083` with an in-memory H2 database, no setup needed.

Frontend:

```bash
cd frontend
npm install
npm start
```

Copy `.env.example` to `.env` first if you're pointing it at a backend that isn't on localhost.

## API

- `POST /api/groups`, `GET /api/groups`, `GET /api/groups/{id}`
- `POST /api/groups/{id}/members`, `GET /api/groups/{id}/members`
- `POST /api/groups/{id}/expenses`, `GET /api/groups/{id}/expenses`
- `GET /api/groups/{id}/balances`
- `GET /api/groups/{id}/settlement`

## Stack

Java 17, Spring Boot 3, Spring Data JPA, H2. React 18, react-router, axios.
