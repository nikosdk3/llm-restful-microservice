#!/bin/bash

# Test Missing Authorization Token

API_URL="http://52.91.2.250:8080/query"

# Make a POST request without the Authorization header
response=$(curl -s -w "%{http_code}" -o response.json -X POST "$API_URL" \
  -H "Content-Type: application/json" \
  -d 'Hello, how are you?')

# Check if the response code is 400 (Bad Request)
if [ "$response" -eq 400 ]; then
  echo "Test Passed: Missing authorization token returned a 400 Bad Request response."
else
  echo "Test Failed: Expected 400 Bad Request, but got $response."
fi