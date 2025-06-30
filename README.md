# Money Transfer API

A simple REST API for internal money transfers between accounts.

---

## How to run

1. Go into the project folder:

```
cd money-transfer
```
2. Start the database via Docker Compose:
```
docker-compose up -d
```
3. In another terminal, run the script:
```
./run-tests.sh
```
---
## Database Tables
1. Account

| Column  | Type      | Description             |
| ------- | --------- | ----------------------- |
| id      | BIGSERIAL | Primary Key             |
| owner   | VARCHAR   | Owner name              |
| balance | NUMERIC   | Current account balance |

Initial seed data (via data.sql):

| id | owner   | balance |
| -- | ------- | ------- |
| 1  | Milly   | 100     |
| 2  | Billy   | 200     |
| 3  | Charlie | 50      |

2. Transfer

Stores successful transfer records. (Initially empty)

   | Column            | Type      | Description                 |
   | ----------------- | --------- | --------------------------- |
   | id                | BIGSERIAL | Primary Key                 |
   | from\_account\_id | BIGINT    | FK to `account`             |
   | to\_account\_id   | BIGINT    | FK to `account`             |
   | amount            | NUMERIC   | Amount transferred          |
   | status            | VARCHAR   | SUCCESS                     |
   | created\_at       | TIMESTAMP | When transfer was performed |
---
### What is tested in run-tests.sh

Runs via curl:
> Current state of account table

>Transfer: Billy → Milly, 100 (should succeed)

>Transfer: Charlie → Milly, 100 (should fail: insufficient funds)

>Transfer: Jenny (id 999) → Milly (should fail: invalid sender)

>Transfer: Charlie → Milly, -50 (should fail: invalid amount)

>Prints final state of accounts.

>Prints all successful transfers from the transfer table.

This demonstrates both positive and negative scenarios.

---
### Possible Data Modeling Improvements
To better support analytical use cases:

1. Make account table slowly changing dimension type 2 (SCD2):
   1. Add columns like valid_from, valid_to, active.

   2. Instead of updating balances in place, insert new rows with updated balance.

   3. The old record is marked inactive.

    Benefits:
    - You get full balance history over time.
    - Analysts can query snapshots of balances at any point.

2. Enhance transfer table with failed attempts of transfers.
   Currently only successful transfers are logged:
   1. Always insert a record, even on failure.

   2. Add columns:
      - status (e.g. SUCCESS / FAILED)
      - error_message (failure reason)

    This enables full tracking of all transfer attempts.

    Note:
   Implementing error logging requires careful transaction management:

   - Successful transfers and their balance updates should be one transaction.

   - Failed attempts must be logged outside the transfer transaction, in a separate safe save (REQUIRES_NEW).

   - This was not implemented fully here to keep the demo simple.