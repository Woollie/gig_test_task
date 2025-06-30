#!/bin/bash
set -e

echo "=== Step 1: Build"
mvn clean package

echo "=== Step 2: Start App"
java -jar target/money-transfer.jar &
APP_PID=$!

echo "=== Waiting for app to start..."
until curl --silent --fail http://localhost:8080/api/accounts > /dev/null; do
  echo "Waiting for app to be ready..."
  sleep 2
done
echo "App is up!"


echo "=== Step 4: Transfers ==="
echo "Current state of accounts:"
curl http://localhost:8080/api/accounts
echo
echo
echo "- Billy -> Milly (100)"
curl -s -X POST -H "Content-Type: application/json" -d '{"fromAccountId":2,"toAccountId":1,"amount":100}' http://localhost:8080/api/transfer
echo
echo
echo "- Charlie -> Milly (100) (should fail because of insufficient funds)"
curl -s -X POST -H "Content-Type: application/json" -d '{"fromAccountId":3,"toAccountId":1,"amount":100}' http://localhost:8080/api/transfer
echo
echo
echo "- Jenny (id 999) -> Milly (50) (should fail because of invalid id)"
curl -s -X POST -H "Content-Type: application/json" -d '{"fromAccountId":999,"toAccountId":1,"amount":50}' http://localhost:8080/api/transfer
echo
echo
echo "- Charlie -> Milly (-50) (should fail because of invalid amount)"
curl -s -X POST -H "Content-Type: application/json" -d '{"fromAccountId":3,"toAccountId":1,"amount":-50}' http://localhost:8080/api/transfer
echo
echo
echo "Current state of accounts:"
curl http://localhost:8080/api/accounts
echo
echo
echo "Logged transfer requests:"
curl -s http://localhost:8080/api/transfers
echo
echo "=== Stopping app ==="
kill $APP_PID


